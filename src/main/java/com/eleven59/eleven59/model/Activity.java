package com.eleven59.eleven59.model;

import org.springframework.data.annotation.Id;

public abstract class Activity{
    @Id
    private String id;
    // MongoDb will handle and we don't have to pass as constructor
    private String name;
    private int urgency;
    private int priority;
    private String userId;

    public Activity(String name, int urgency, int priority, String userId) {
        this.name = name;
        this.urgency = urgency;
        this.priority = priority;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getUserId() {
        return userId;
    }

    public int getWeight(){
        return urgency * priority;
    }

    public int getUrgency() {
        return urgency;
    }

    public int getPriority() {
        return priority;
    }

//    @Override
//    public int compareTo(Activity a){
//        int weightDiff = Double.compare(this.getWeight(),a.getWeight());
//        if(weightDiff != 0) return weightDiff;
//
//        int priorityDiff = this.getPriority() - a.getPriority();
//        if(priorityDiff != 0) return priorityDiff;
//
//        return this.getUrgency() - a.getUrgency();
//
//    }

}
