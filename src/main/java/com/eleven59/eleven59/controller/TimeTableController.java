package com.eleven59.eleven59.controller;

import com.eleven59.eleven59.Exceptions.IlligalUserException;
import com.eleven59.eleven59.model.TimeTable;
import com.eleven59.eleven59.services.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/timetable")
public class TimeTableController {

    @Autowired
    TimeTableService timeTableService;

    @GetMapping
    public ResponseEntity<List<TimeTable>> getTimeTable(){
        return ResponseEntity.ok(timeTableService.getAllTimeTable());
    }

    @GetMapping("/{userId}")
    public List<TimeTable> getUserTimeTable(@PathVariable String userId){
        return timeTableService.getUserTimeTable(userId);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> byId(@PathVariable String id) throws IOException {
        try {
            return ResponseEntity.ok(timeTableService.getICS(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error Occured Generating ICS");
        }
    }

    @PostMapping
    public ResponseEntity<Object> generate(@RequestBody Map<String, Object> payload){
         String userId = (String) payload.get("userId");
        try {
            TimeTable generaredTime = timeTableService.generateTimeTable(userId);
            return ResponseEntity.ok(generaredTime);
        } catch (IlligalUserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

}
