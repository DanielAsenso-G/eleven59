package com.eleven59.eleven59.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "courses")

public class Course extends RoutineActivity {
    static final int URGENCY_MAX = 6;
    static final int PRIORITY_MAX = 6;
    private double avgStudyHour;
    private int performanceIndex;

    public Course(String name, String userId, List<TimeSlot> schedule, int performanceIndex) {
        super(name, URGENCY_MAX, PRIORITY_MAX, userId, schedule);
        this.performanceIndex = performanceIndex;
    }

    public double getAvgStudyHour() {
        return avgStudyHour;
    }

    public void setAvgStudyHour(double avgStudyHour) {
        this.avgStudyHour = avgStudyHour;
    }

    public int getPerformanceIndex() {
        return performanceIndex;
    }

    public void setPerformanceIndex(int performanceIndex) {
        this.performanceIndex = performanceIndex;
    }

}
