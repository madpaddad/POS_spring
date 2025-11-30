package com.example.demo.auth;

import com.example.demo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthRepository extends MongoRepository<User, String> {
    User findByPhoneNumber(String phoneNumber);
}
