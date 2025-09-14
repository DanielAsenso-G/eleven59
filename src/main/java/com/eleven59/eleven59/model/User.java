package com.eleven59.eleven59.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalTime;
import java.util.List;


public abstract class User {
    @Id
    private String id;
    // MongoDb will handle and we don't have to pass as constructor

    @Indexed(unique = true)
    private String email;
    private String name;
    private LocalTime bedTime;
    private LocalTime getUpTime;
    private String studyHabit;

    public User(String email, String name, LocalTime bedTime, LocalTime getUpTime, String studyHabit) {
        this.email = email;
        this.name = name;
        this.bedTime = bedTime;
        this.getUpTime = getUpTime;
        this.studyHabit = studyHabit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getBedTime() {
        return bedTime;
    }

    public void setBedTime(LocalTime bedTime) {
        this.bedTime = bedTime;
    }

    public LocalTime getGetUpTime() {
        return getUpTime;
    }

    public void setGetUpTime(LocalTime getUpTime) {
        this.getUpTime = getUpTime;
    }

    public String getStudyHabit() {
        return studyHabit;
    }

    public void setStudyHabit(String studyHabit) {
        this.studyHabit = studyHabit;
    }

    abstract public List<LocalTime> getStudyInterval();

    @Override
    public String toString() {
        return """
            User{
                _id='%s'
                email='%s'
                name='%s',
                bedTime=%s,
                getUpTime=%s,
                studyHabit='%s'
            }""".formatted(id,email,name, bedTime, getUpTime, studyHabit);
    }

}
