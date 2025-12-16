package com.example.demo.repository;

import com.example.demo.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@EnableMongoRepositories
public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {

    Mono<Boolean> existsByName(String name);
    Flux<Category> findAll();
}
