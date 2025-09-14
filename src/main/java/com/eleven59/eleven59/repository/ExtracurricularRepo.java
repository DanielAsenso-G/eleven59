package com.eleven59.eleven59.repository;
import com.eleven59.eleven59.model.RoutineActivity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExtracurricularRepo extends MongoRepository<RoutineActivity, String> {
  List<RoutineActivity> findByUserId(String userId);
}
