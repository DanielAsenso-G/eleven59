package com.eleven59.eleven59.services;

import com.eleven59.eleven59.model.DayUser;
import com.eleven59.eleven59.model.NightUser;
import com.eleven59.eleven59.model.User;
import com.eleven59.eleven59.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class UserServices  {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User createUser(Map<String, Object> userData){
      String name = (String) userData.get("name");
      String email = (String) userData.get("email");
      String bedTime = (String) userData.get("bedTime");
      String getUpTime = (String) userData.get("getUpTime");
      String studyHabbit = (String) userData.get("studyHabit");

      String[] start = bedTime.split(":");
      String[] end = getUpTime.split(":");

      User newUser;
      if(studyHabbit.equals("night")){
          newUser = new NightUser(email, name,
                  LocalTime.of(Integer.parseInt(start[0]),Integer.parseInt(start[1])),
                  LocalTime.of(Integer.parseInt(end[0]),Integer.parseInt(end[1]))
                  );
      }else{
          newUser = new DayUser(email, name,
                  LocalTime.of(Integer.parseInt(start[0]),Integer.parseInt(start[1])),
                  LocalTime.of(Integer.parseInt(end[0]),Integer.parseInt(end[1]))
                  );
      }
        return userRepository.save(newUser);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }



}
