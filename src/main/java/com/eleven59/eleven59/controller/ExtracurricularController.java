package com.eleven59.eleven59.controller;

import com.eleven59.eleven59.model.RoutineActivity;
import com.eleven59.eleven59.services.ExtracurricularServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/extracurricular")
public class ExtracurricularController {

    @Autowired
    ExtracurricularServices extracurricularService;

    @GetMapping
    public ResponseEntity<List<RoutineActivity>> getAllActivities(){
        return ResponseEntity.ok(extracurricularService.getAllActivities()) ;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<RoutineActivity>> getAllActsByUser(@PathVariable String userId){
        return ResponseEntity.ok(extracurricularService.getByUser(userId));
    }

    @PostMapping
    public ResponseEntity<RoutineActivity> addActivity(@RequestBody Map<String, Object> payload){
        return ResponseEntity.ok(extracurricularService.add(payload));
    }
}
