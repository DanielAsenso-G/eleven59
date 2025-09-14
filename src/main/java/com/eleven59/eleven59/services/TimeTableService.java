package com.eleven59.eleven59.services;

import com.eleven59.eleven59.Exceptions.IlligalUserException;
import com.eleven59.eleven59.model.*;
import com.eleven59.eleven59.repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class TimeTableService {
    @Autowired
    TimeTableRepository timeTableRepository;

    @Autowired
    CourseServices courseServices;

    @Autowired
    ExtracurricularServices extracurricularServices;

    @Autowired
    UserServices userServices;

    public List<TimeTable> getAllTimeTable(){
        return timeTableRepository.findAll();
    }

    public List<TimeTable> getUserTimeTable(String userId){
        return timeTableRepository.findByUserId(userId);
    }

    public TimeTable generateTimeTable(String userId) throws IlligalUserException {
        User user = userServices.getUserByEmail(userId);

        if(user == null){ throw new IlligalUserException(userId);}

        TimeTable timeTable = new TimeTable(user.getEmail());

        List<Course> courses = courseServices.coursesByUser(user.getEmail());
        List<RoutineActivity> extracurricular = extracurricularServices.getByUser(user.getEmail());


        // let block courses first
        timeTable.blockCourse(courses);

        // Let block sleeping time too
        List<TimeSlot> sleepTimes= new ArrayList<>();
        for(int i = 0; i < 7; i++){
            sleepTimes.add(new TimeSlot(i, user.getBedTime(), user.getGetUpTime()));
        }
        RoutineActivity sleep = new RoutineActivity("Sleeping", 5, 5, "UserID", sleepTimes);
        timeTable.block(sleep);


        // We can block actovies which is very important -- above 20
        Predicate<RoutineActivity> heavyWeight = (a) -> a.getWeight() >= 20;
        List<RoutineActivity> heavyWeightActivity = extracurricular.stream().filter(heavyWeight).collect(Collectors.toList());
        timeTable.block(heavyWeightActivity);


        // Let fetch all available time so that we can block the study session for each course
        Map<Integer, Map<LocalTime, Long>> freeStudyTime =  timeTable.getFreeTimeForStudy(user.getStudyInterval().get(0), user.getStudyInterval().get(1));

        // Block study session
        Comparator<Course> courseComparator = (a, b) -> a.getPerformanceIndex() - b.getPerformanceIndex();
        courses.sort(courseComparator);
        timeTable.setStudyHours(
                courses,
                freeStudyTime
        );


        // Now let block the remaining activities
        Predicate<RoutineActivity> lightWeight = (a) -> a.getWeight() < 20;
        List<RoutineActivity> lightWeightActivity = extracurricular.stream().filter(lightWeight).toList();
        timeTable.block(lightWeightActivity);

        return timeTableRepository.save(timeTable);

    }

    public String getICS(String id) throws IOException {
        Optional<TimeTable> res = timeTableRepository.findById(id);

        if (res.isEmpty()) {
            throw new IOException();
        }

        TimeTable timeTable = res.get();
        String icsContent = timeTable.generateICS(id + ".ics", 0);
        return icsContent;
    }

}
