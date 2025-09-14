package com.eleven59.eleven59.services;

import com.eleven59.eleven59.model.Course;
import com.eleven59.eleven59.model.RoutineActivity;
import com.eleven59.eleven59.model.TimeSlot;
import com.eleven59.eleven59.repository.ExtracurricularRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExtracurricularServices {
    @Autowired
    ExtracurricularRepo extracurricularRepo;


    public List<RoutineActivity> getAllActivities() {
        return extracurricularRepo.findAll();
    }

    public List<RoutineActivity> getByUser(String userId) {
        return extracurricularRepo.findByUserId(userId);
    }

    public RoutineActivity add(Map<String, Object> payload) {
        String name = (String) payload.get("name");
        String userId = (String) payload.get("userId");
        int urgency = (int) payload.get("urgency");
        int priority = (int) payload.get("priority");

        List<Map<Integer, Object>> rawSchedules = (List<Map<Integer, Object>>) payload.get("schedules");

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

        RoutineActivity newActivity = new RoutineActivity(name, urgency, priority, userId, schedules);
        return extracurricularRepo.save(newActivity);
    }
}
