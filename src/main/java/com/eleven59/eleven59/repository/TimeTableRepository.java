package com.eleven59.eleven59.repository;

import com.eleven59.eleven59.model.TimeTable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TimeTableRepository extends MongoRepository<TimeTable, String> {
    List<TimeTable> findByUserId(String userId);
    Optional<TimeTable> findById(String id);
}
