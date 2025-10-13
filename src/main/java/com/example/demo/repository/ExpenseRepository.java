package com.example.demo.repository;


import com.example.demo.model.Expense;
import com.example.demo.model.ExpenseCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;

@EnableMongoRepositories()
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    @Query("{'name': ?0}")
    Optional<Expense> findByName(String name);
}
