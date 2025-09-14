package com.eleven59.eleven59.services;

import com.eleven59.eleven59.model.Course;
import com.eleven59.eleven59.model.TimeSlot;
import com.eleven59.eleven59.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CourseServices {

    @Autowired
    CourseRepository courseRepository;

    public List<Course> allCourses(){
        return courseRepository.findAll();
    }

    public List<Course> coursesByUser(String userId){
        return courseRepository.findByUserId(userId);
    }

    public Course createNewCourse(Map<String, Object> courseData){

        String name = (String) courseData.get("name");
        String userId = (String) courseData.get("userId");
        int performanceIndex = (int) courseData.get("performanceIndex");
        List<Map<Integer, Object>> rawSchedules = (List<Map<Integer, Object>>) courseData.get("schedules");

        List<TimeSlot> schedules = new ArrayList<>();

        for (Map<Integer, Object> schedule : rawSchedules) {

            int day = (int) schedule.get("day");

            String startTime = ((String) schedule.get("start"));
            String endTime = (String) schedule.get("end");

            String[] start = startTime.split(":");
            String[] end = endTime.split(":");

            schedules.add(new TimeSlot(
                    day,
                    LocalTime.of(Integer.parseInt(start[0]),Integer.parseInt(start[1])),
                    LocalTime.of(Integer.parseInt(end[0]),Integer.parseInt(end[1]))
            ));

        }

        Course newCourse = new Course(name, userId, schedules, performanceIndex);

       return courseRepository.save(newCourse);

    }


}
