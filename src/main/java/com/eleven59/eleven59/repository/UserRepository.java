package com.eleven59.eleven59.repository;

import com.eleven59.eleven59.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
