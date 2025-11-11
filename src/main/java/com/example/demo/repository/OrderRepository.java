package com.example.demo.repository;

import com.example.demo.model.Order;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Optional;
import com.example.demo.dto.order.orderDTO;

//import java.util.List;

@EnableMongoRepositories()
public interface OrderRepository extends MongoRepository<Order, String> {

        @Aggregation(pipeline = {
                "{ $match: { _id: ?0 } }",
                "{ $lookup: { from: 'order_items', localField: 'orderItemsIds', foreignField: '_id', as: 'orderItems' } }",
                "{ $unwind: { path: '$orderItems', preserveNullAndEmptyArrays: true } }",
                "{ $lookup: { from: 'product', localField: 'orderItems.product_id', foreignField: '_id', as: 'orderItems.product' } }",
                "{ $group: { _id: '$_id', tableNo: { $first: '$tableNo' }, orderStatus: { $first: '$orderStatus' }, orderItems: { $push: '$orderItems' } } }"
        })
        orderDTO findOrderById(String id);

}
