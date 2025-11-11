package com.example.demo.repository;

import com.example.demo.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
public interface CategoryRepository extends MongoRepository<Category, String>{

    boolean existsByName(String name);
}
