package com.eleven59.eleven59.model;

import com.eleven59.eleven59.config.LocalTimeConverter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Document("timetable")
public class TimeTable {
    @Id
    private String id;
    @Field("calendar")
    private Map<Integer, TreeMap<LocalTime, String>> calendar = new TreeMap<>();

//    private Map<TimeSlot, String> clashingEvent = new TreeMap<>();
    private String userId;
    private final int DEFAULT_TIME_INTERVAL_MINUTES = 5;

    public TimeTable(String userId) {
        for (int i = 0; i < 7; i++) {
            calendar.put(i, new TreeMap<>());
        }
        this.userId = userId;
    }

    // getters
    public Map<Integer, TreeMap<LocalTime, String>> getCalendar() {
        return calendar;
    }

    public String getId() {
        return id;
    }

    // Other methos

    private boolean isConflict(int day, LocalTime start, LocalTime end) {
        Map<LocalTime, String> currentDay = calendar.get(day);

        LocalTime currentTime = start;
        while (!currentTime.isAfter(end)) {
            if (currentDay.getOrDefault(currentTime, null) != null) {
                return true;
            }
            currentTime = currentTime.plusMinutes(DEFAULT_TIME_INTERVAL_MINUTES);
        }
        return false;
    }

    public void block(TimeSlot slot, String activity) {
        int startDay = slot.getDay();
        LocalTime startTime = slot.getStart();
        LocalTime endTime = slot.getEnd();
        int endDay = (endTime.isBefore(startTime)) ? (startDay + 1) % 7 : startDay;

        if (startDay == endDay) {
            blockSingleDay(startDay, startTime, endTime, activity);
        } else {
            blockSingleDay(startDay, startTime, LocalTime.of(23, 54), activity);

            blockSingleDay(endDay, LocalTime.of(0, 0), endTime, activity);

        }
    }

    public void block(List<RoutineActivity> routineActivities) {

        for (RoutineActivity routineActivity : routineActivities) {

            for (TimeSlot timeSlot : routineActivity.getSchedule()) {
                blockSingleDay(timeSlot.getDay(), timeSlot.getStart(), timeSlot.getEnd(), routineActivity.getName());
            }

        }
    }

    public void block(RoutineActivity activities) {
        for (TimeSlot slot : activities.getSchedule()) {
            block(slot, activities.getName());
        }
    }

    private void blockSingleDay(int day, LocalTime startTime, LocalTime endTime, String activity) {
        if (isConflict(day, startTime, endTime)) {
//            clashingEvent.put(new TimeSlot(day, startTime, endTime), activity);
            return;
        }

        Map<LocalTime, String> currentDay = calendar.get(day);
        LocalTime currentTime = startTime;
        while (!currentTime.isAfter(endTime)) {

            currentDay.put(currentTime, activity);
            currentTime = currentTime.plusMinutes(DEFAULT_TIME_INTERVAL_MINUTES);
        }
    }

    public void blockCourse(List<Course> courses) {

        for (Course course : courses) {

            for (TimeSlot timeSlot : course.getSchedule()) {
                blockSingleDay(timeSlot.getDay(), timeSlot.getStart(), timeSlot.getEnd(), course.getName());
            }

        }
    }

    public void resolveConflict(TimeSlot slot, String activity) {
//        clashingEvent.remove(slot);
        block(slot, activity);
    }

    public Map<Integer, Map<LocalTime, Long>> getFreeTimeForStudy(LocalTime start, LocalTime end) {

         Map<Integer, Map<LocalTime, Long>> availbleTimes = new TreeMap<>();

         for (int i = 0; i < 7; i++) {

            Map<LocalTime, Long> freeTimes = new TreeMap<>();
            Map<LocalTime, String> currentDay = calendar.get(i);

            LocalTime left = start;
            LocalTime right = start;

            while (right.isBefore(end)) {

                long diff = Duration.between(left, right).toMinutes();
                while(((currentDay.getOrDefault(right, null) == null)) && right.isBefore(end) ) {

                    if(diff == 120) break;
                    diff = Duration.between(left, right).toMinutes();
                    right = right.plusMinutes(DEFAULT_TIME_INTERVAL_MINUTES);
                }

                if(left != right){
                    freeTimes.put(left, diff);
                }

                right = right.plusMinutes(DEFAULT_TIME_INTERVAL_MINUTES);
                left = right;
            }


             availbleTimes.put(i, freeTimes);
        }



       return availbleTimes;
    }

    public void studyBlocker(Course course, Map<Integer, Map<LocalTime, Long>> availableTimes, Long minutes) {
        for (TimeSlot slot : course.getSchedule()) {
            int day = (slot.getDay() + 6) % 7;
            Long studyMinutes = minutes;
            Map<LocalTime, Long> freeTimesThatDay = availableTimes.get(day);


            LocalTime bestStartTime = null;
            Long maxAvailableMinutes = 0l;

            for (Map.Entry<LocalTime, Long> availableSlot : freeTimesThatDay.entrySet()) {
                if (availableSlot.getValue() > maxAvailableMinutes) {
                    maxAvailableMinutes = availableSlot.getValue();
                    bestStartTime = availableSlot.getKey();
                }
            }


            if (bestStartTime != null) {
                Long actualStudyMinutes = Math.min(studyMinutes, maxAvailableMinutes);
                blockSingleDay(day, bestStartTime, bestStartTime.plusMinutes(actualStudyMinutes), course.getName()+" - Study Session");
                freeTimesThatDay.remove(bestStartTime);
                availableTimes.put(day, freeTimesThatDay);
                return;
            }


        }
    }

    public void setStudyHours(List<Course> courses, Map<Integer, Map<LocalTime, Long>> availableTimes) {
        for (Course course : courses) {
            int index = course.getPerformanceIndex();

            if (index == 1) {
                studyBlocker(course, availableTimes, 120l);
            } else if (index == 2) {
                studyBlocker(course, availableTimes, 90l);
            } else {
                studyBlocker(course, availableTimes, 60l);
            }
        }
    }

    public String generateICS(String filename, int weeksAhead) throws IOException {
        try  {
            Path filePath = Paths.get("src/main/resources/static/timetable", filename);
            FileWriter writer = new FileWriter(filePath.toFile());
            writer.write("BEGIN:VCALENDAR\n");
            writer.write("VERSION:2.0\n");
            writer.write("PRODID:-//Ashesi Student Time Optimizer//NONSGML v1.0//EN\n");


            LocalDate currentDate = LocalDate.now();
            LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
            LocalDate targetStartDate = startOfWeek.plusWeeks(weeksAhead);

            for (Map.Entry<Integer, TreeMap<LocalTime, String>> dayEntry : calendar.entrySet()) {

                TreeMap<LocalTime, String> daySchedule = dayEntry.getValue();
                List<Object> schdule = transform(daySchedule);
                LocalDate eventDate = targetStartDate.plusDays((dayEntry.getKey() - 1) % 7);

                for(Object slots: schdule){
                    List<Object> activtyTime = (List<Object>) slots;
                    LocalTime startTime = LocalTime.parse(activtyTime.get(0).toString());
                    LocalTime endTime = LocalTime.parse(activtyTime.get(1).toString());
                    String activity = activtyTime.get(2).toString();

                    writeEvent(writer, eventDate, startTime, endTime, activity);

                }
            }

            writer.write("END:VCALENDAR\n");
            writer.flush();
            writer.close();
            return filename;
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    private void writeEvent(FileWriter writer, LocalDate eventDate, LocalTime startTime, LocalTime endTime, String activity) throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

        String startFormatted = startTime.atDate(eventDate).format(dateTimeFormatter);
        String endFormatted = endTime.atDate(eventDate).format(dateTimeFormatter);

        writer.write("BEGIN:VEVENT\n");
        writer.write("SUMMARY:" + activity + "\n");
        writer.write("DTSTART:" + startFormatted + "\n");
        writer.write("DTEND:" + endFormatted + "\n");
        writer.write("BEGIN:VALARM\n");
        writer.write("TRIGGER:-PT15M\n");
        writer.write("ACTION:DISPLAY\n");
        writer.write("DESCRIPTION:Reminder for " + activity + "\n");
        writer.write("END:VALARM\n");
        writer.write("END:VEVENT\n");

    }

    private List<Object> transform(Map<LocalTime, String> timetable){
        List<Object> result = new ArrayList<>();

        LocalTime startTime = null;
        LocalTime lastTime = null;
        String lastActivity = null;

        List<LocalTime> sortedTimes = timetable.keySet().stream().collect(Collectors.toList());

        for (LocalTime time : sortedTimes) {
            String activity = timetable.get(time);

            // Initialize start time and activity
            if (startTime == null) {
                startTime = time;
                lastActivity = activity;
            }

            // If activity changes, finalize the previous group and start a new one
            if (!activity.equals(lastActivity)) {
                result.add(
                        new ArrayList<>(
                                Arrays.asList(
                                        startTime,
                                        lastTime,
                                        lastActivity
                                )
                        )
                );
                startTime = time;
                lastActivity = activity;
            }

            lastTime = time;
        }

        // Add the last group
        if (startTime != null && lastActivity != null) {
            result.add(
                    new ArrayList<>(
                            Arrays.asList(
                                    startTime,
                                    lastTime,
                                    lastActivity
                            )
                    )
            );
        }

        return result;

    }



    @Override
    public String toString() {
        return "TimeTable{" +
                "calendar=" + calendar +
//                ", clashingEvent=" + clashingEvent +
                ", DEFAULT_TIME_INTERVAL_MINUTES=" + DEFAULT_TIME_INTERVAL_MINUTES +
                '}';
    }
}
