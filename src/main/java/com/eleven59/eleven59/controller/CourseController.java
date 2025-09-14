package com.eleven59.eleven59.controller;

import com.eleven59.eleven59.model.Course;
import com.eleven59.eleven59.services.CourseServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

  @Autowired
  CourseServices courseServices;

    @GetMapping("/")
    public ResponseEntity<List<Course>> getAllCourse(){
      return ResponseEntity.ok(courseServices.allCourses());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Course>> getAllCourseByUser(@PathVariable String userId){
      return ResponseEntity.ok(courseServices.coursesByUser(userId));
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Map<String, Object> payload){
      return ResponseEntity.ok(courseServices.createNewCourse(payload));
    }



}
