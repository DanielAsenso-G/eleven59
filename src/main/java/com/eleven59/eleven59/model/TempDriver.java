package com.eleven59.eleven59.model;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TempDriver {

    public static void main(String[] args) throws IOException {
        TimeTable timeTable = new TimeTable("sada");

        // user
        User user = new NightUser("sfaasf@dsf.sfd", "kofi", LocalTime.of(22, 45),
                LocalTime.of(6, 30) );

        // create a list of courses
        List<Course> courses = new ArrayList<>(List.of(
                // Course 1: Introduction to Programming
                new Course(
                        "Introduction to Programming",
                        "user123",
                        List.of(
                                new TimeSlot(1, LocalTime.of(10, 0), LocalTime.of(12, 0)), // Monday 10 AM - 12 PM
                                new TimeSlot(3, LocalTime.of(10, 0), LocalTime.of(12, 0)), // Wednesday 10 AM - 12 PM
                                new TimeSlot(5, LocalTime.of(10, 0), LocalTime.of(12, 0))  // Friday 10 AM - 12 PM
                        ),
                        10
                ),

                // Course 2: Data Structures
                new Course(
                        "Data Structures",
                        "user123",
                        List.of(
                                new TimeSlot(1, LocalTime.of(13, 0), LocalTime.of(15, 0)), // Monday 1 PM - 3 PM
                                new TimeSlot(3, LocalTime.of(13, 0), LocalTime.of(15, 0)), // Wednesday 1 PM - 3 PM
                                new TimeSlot(5, LocalTime.of(13, 0), LocalTime.of(15, 0))  // Friday 1 PM - 3 PM
                        ),
                        12
                ),

                // Course 3: Calculus
                new Course(
                        "Calculus",
                        "user123",
                        List.of(
                                new TimeSlot(2, LocalTime.of(9, 0), LocalTime.of(11, 0)),  // Tuesday 9 AM - 11 AM
                                new TimeSlot(4, LocalTime.of(9, 0), LocalTime.of(11, 0)),  // Thursday 9 AM - 11 AM
                                new TimeSlot(5, LocalTime.of(9, 0), LocalTime.of(11, 0))   // Friday 9 AM - 11 AM
                        ),
                        8
                ),

                // Course 4: Physics
                new Course(
                        "Physics",
                        "user123",
                        List.of(
                                new TimeSlot(2, LocalTime.of(11, 0), LocalTime.of(13, 0)), // Tuesday 11 AM - 1 PM
                                new TimeSlot(4, LocalTime.of(11, 0), LocalTime.of(13, 0))  // Thursday 11 AM - 1 PM
                        ),
                        15
                ),

                // Course 5: English Literature
                new Course(
                        "English Literature",
                        "user123",
                        List.of(
                                new TimeSlot(1, LocalTime.of(14, 0), LocalTime.of(16, 0)), // Monday 2 PM - 4 PM
                                new TimeSlot(3, LocalTime.of(14, 0), LocalTime.of(16, 0)), // Wednesday 2 PM - 4 PM
                                new TimeSlot(5, LocalTime.of(14, 0), LocalTime.of(16, 0))  // Friday 2 PM - 4 PM
                        ),
                        7
                )
        ));



        // list routine activites
        List<RoutineActivity> routineActivities = List.of(
                // Activity 1: Oral Presentation
                new RoutineActivity(
                        "Oral Presentation",
                        5, // Urgency
                        5, // Priority
                        "user123",
                        List.of(
                                new TimeSlot(4, LocalTime.of(11, 40), LocalTime.of(13, 55))  // Thursday 11:40 AM - 1:55 PM
                        )
                ),

                // Activity 2: Gym Session
                new RoutineActivity(
                        "Gym Session",
                        3, // Urgency
                        3, // Priority
                        "user123",
                        List.of(
                                new TimeSlot(1, LocalTime.of(6, 30), LocalTime.of(7, 30)),  // Monday 6:30 AM - 7:30 AM
                                new TimeSlot(3, LocalTime.of(6, 30), LocalTime.of(7, 30)),  // Wednesday 6:30 AM - 7:30 AM
                                new TimeSlot(5, LocalTime.of(6, 30), LocalTime.of(7, 30))   // Friday 6:30 AM - 7:30 AM
                        )
                ),

                // Activity 3: Study Group
                new RoutineActivity(
                        "Study Group",
                        2, // Urgency
                        4, // Priority
                        "user123",
                        List.of(
                                new TimeSlot(2, LocalTime.of(14, 0), LocalTime.of(16, 0)),  // Tuesday 2:00 PM - 4:00 PM
                                new TimeSlot(4, LocalTime.of(14, 0), LocalTime.of(16, 0))   // Thursday 2:00 PM - 4:00 PM
                        )
                ),

                // Activity 4: Weekly Planning
                new RoutineActivity(
                        "ASE Meetings",
                        5, // Urgency
                        5, // Priority
                        "user123",
                        List.of(
                                new TimeSlot(0, LocalTime.of(18, 0), LocalTime.of(19, 0))  // Sunday 6:00 PM - 7:00 PM
                        )
                ),

                // Activity 5: Online Course Webinar
                new RoutineActivity(
                        "Volley Ball Training",
                        3, // Urgency
                        1, // Priority
                        "user123",
                        List.of(
                                new TimeSlot(6, LocalTime.of(8, 0), LocalTime.of(17, 0))  // Tuesday 10:00 AM - 12:00 PM// Thursday 10:00 AM - 12:00 PM
                        )
                )
        );


     timeTable.blockCourse(courses);

     // creating every day
        List<TimeSlot> sleepTimes= new ArrayList<>(); // Creating of list of Sleep Times
        for(int i = 0; i < 7; i++){
            sleepTimes.add(new TimeSlot(i, user.getBedTime(), user.getGetUpTime()));
        }
        RoutineActivity sleep = new RoutineActivity("Sleeping", 5, 5, "UserID", sleepTimes);
        timeTable.block(sleep);

        // block actovies above 20
        Predicate<RoutineActivity> heavyWeight = (a) -> a.getWeight() >= 20;
        List<RoutineActivity> heavyWeightActivity = routineActivities.stream().filter(a-> heavyWeight.test(a)).collect(Collectors.toList());
        timeTable.block(heavyWeightActivity);


// get available hours for blaocking stydy session
        Map<Integer, Map<LocalTime, Long>> freeStudyTime =  timeTable.getFreeTimeForStudy(user.getStudyInterval().get(0), user.getStudyInterval().get(1));

        // Block study session
        Comparator<Course> courseComparator = (a, b) -> a.getPerformanceIndex() - b.getPerformanceIndex();
        courses.sort(courseComparator);

        timeTable.setStudyHours(
                courses,
                freeStudyTime
        );

        Predicate<RoutineActivity> lightWeight = (a) -> a.getWeight() < 20;
        List<RoutineActivity> lightWeightActivity = routineActivities.stream().filter(lightWeight).toList();
        timeTable.block(lightWeightActivity);

        timeTable.generateICS("nextweek2.ics",1);
      //  timeTable.generateICS("nextTwoWeeks2.ics",2);
    }
}
