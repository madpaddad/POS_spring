package com.example.demo.repository;

import com.example.demo.dto.order.ProductDTO;
import com.example.demo.model.Product;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Optional;

@EnableMongoRepositories
public interface ProductRepository extends MongoRepository<Product, String>{

    @Query("{ 'category_id' : ?0 }")
    List<Product> findByCategoryIdCustom(String categoryId);

//    @Aggregation(pipeline= {
//        "{ $lookup: { from : 'product', localField: 'id', 'foreignField: '_id', as: 'id'} }",
//    })
//    ProductDTO findProducts();

//    @Aggregation(pipeline = {
//            "{ $match: { _id: ?0 } }",
//            "{ $lookup: { from: 'order_items', localField: 'orderItemsIds', foreignField: '_id', as: 'orderItems' } }",
//            "{ $unwind: { path: '$orderItems', preserveNullAndEmptyArrays: true } }",
//            "{ $lookup: { from: 'product', localField: 'orderItems.product_id', foreignField: '_id', as: 'orderItems.product' } }",
//            "{ $group: { _id: '$_id', tableNo: { $first: '$tableNo' }, orderStatus: { $first: '$orderStatus' }, orderItems: { $push: '$orderItems' } } }"
//    })
//    orderDTO findOrderById(String id);
}
