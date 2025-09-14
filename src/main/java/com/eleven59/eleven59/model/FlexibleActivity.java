package com.eleven59.eleven59.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "extracurricular")

public class FlexibleActivity extends Activity {
    private double estimatedTime;

    public FlexibleActivity(String name, int urgency, int priority, String userId, double estimatedTime) {
        super(name, urgency, priority, userId);
        this.estimatedTime = estimatedTime;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
