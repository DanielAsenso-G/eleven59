package com.eleven59.eleven59.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.List;

@Document(collection = "users")
public class DayUser extends User {
    private final LocalTime ENDDAY = LocalTime.of(19, 0);

    public DayUser(String email, String name, LocalTime bedTime, LocalTime getUpTime) {
        super(email, name, bedTime, getUpTime, "day");
    }

    @Override
    public List<LocalTime> getStudyInterval() {
        LocalTime studyStartTime = super.getGetUpTime().plusHours(1);
        return List.of(studyStartTime, ENDDAY);
    }


}
