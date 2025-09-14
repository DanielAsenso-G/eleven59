package com.eleven59.eleven59.repository;

import com.eleven59.eleven59.model.Course;
import com.eleven59.eleven59.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String> {
    List<Course> findByUserId(String userId);
}
