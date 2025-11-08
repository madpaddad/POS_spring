package com.example.demo.repository;

import com.example.demo.model.Order;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Optional;

@EnableMongoRepositories
public interface ProductRepository extends MongoRepository<>{

    @Aggregation(pipeline= {
        "{ $lookup: {from : 'category'}}"
    })
    
}
