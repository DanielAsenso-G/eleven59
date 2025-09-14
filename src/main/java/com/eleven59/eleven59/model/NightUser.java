package com.eleven59.eleven59.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.List;

@Document(collection = "users")
public class NightUser extends User {
    private final LocalTime STARTDAY = LocalTime.of(17, 0);

    public NightUser(String email, String name, LocalTime bedTime, LocalTime getUpTime) {
        super(email, name, bedTime, getUpTime, "night");
    }
    @Override
    public List<LocalTime> getStudyInterval() {
        // Subtract one hour from bedTime using minusHours
        return List.of(STARTDAY, super.getBedTime().minusHours(1));
    }
}
