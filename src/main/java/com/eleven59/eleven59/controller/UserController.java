package com.eleven59.eleven59.controller;

import com.eleven59.eleven59.model.User;
import com.eleven59.eleven59.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserServices userServices;

    @GetMapping
    public ResponseEntity<List<User>> allUsers(){
        List<User> allUsers = userServices.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        User foundUser = userServices.getUserByEmail(email);
        return ResponseEntity.ok(foundUser);
    }

    @PostMapping()
    public ResponseEntity<User> register(@RequestBody Map<String, Object> payload){
        return ResponseEntity.ok(userServices.createUser(payload));
    }


}
