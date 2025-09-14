package com.eleven59.eleven59.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "routine_activities")
public class RoutineActivity extends Activity{
    private List<TimeSlot> schedule;

    public RoutineActivity(String name, int urgency, int priority, String userId, List<TimeSlot> schedule) {
        super(name, urgency, priority, userId);
        this.schedule = schedule;
    }

    public List<TimeSlot> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<TimeSlot> schedule) {
        this.schedule = schedule;
    }
}
