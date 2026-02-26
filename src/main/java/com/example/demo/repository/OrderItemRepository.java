package com.example.demo.repository;

import com.example.demo.model.OrderItem;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface OrderItemRepository extends ReactiveMongoRepository<OrderItem, String> {


}
