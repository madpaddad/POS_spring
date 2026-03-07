package com.example.demo.services;

import com.example.demo.dto.order.UpdateOrderStatus;
import com.example.demo.helper.ApiResponse;
import com.example.demo.model.*;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.dto.order.orderDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.bson.Document;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final OrderItemRepository orderItemRepository;

    public OrderService(
            OrderRepository orderRepository,
            ReactiveMongoTemplate reactiveMongoTemplate,
            OrderItemRepository orderItemRepository) {

        this.orderRepository = orderRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.orderItemRepository = orderItemRepository;
    }

    public Mono<ApiResponse<List<Document>>> get(String id) {

            Aggregation aggregation  = Aggregation.newAggregation(
                    Aggregation.lookup("product", "product_id", "_id", "product"),
                    Aggregation.unwind("product", true),

                    Aggregation.group("order_id")
                            .sum("total").as("total_price")
                            .push(
                                    new BasicDBObject()
                                            .append("id", new BasicDBObject("$toString", "$_id"))
                                            .append("product", "$product.name")
                                            .append("quantity", "$quantity")
                                            .append("total", "$total")
                            )
                            .as("products"),

                    Aggregation.lookup("order", "order_id", "_id", "order"),
                    Aggregation.unwind("order", true),
                    Aggregation.addFields()
                            .addField("order_status")
                            .withValue("$order.orderStatus")
                            .build()
            );


            // There are many data -> it will return as flux
            return reactiveMongoTemplate.aggregate(aggregation, "order_items", Document.class)
                .collectList()
                .map(data -> ApiResponse.success(data, "GET"));
    }

    /*

    @params table: create an order

    Creating an order is creating a table that is occupied with an order
    */

    public Mono<Boolean> create(String table){

        Order order = new Order(table, OrderStatus.NEW);

        return reactiveMongoTemplate
                .insert(order)
                .map(Order::getId)
                .flatMap(id ->
                {
                    TableOrder tableorder = new TableOrder(table, id);
                    return reactiveMongoTemplate.insert(tableorder);

                })
                .map( tableorder -> {
                    Query query = new Query(Criteria.where("_id").is(table));
                    Update update = new Update();

                    update.set("tableStatus", "OCCUPIED");

                    return reactiveMongoTemplate.updateFirst(query, update, Table.class);
                }
                )
                .map(saved -> true)
                .onErrorReturn(false);
    }

    /*
    @ param id : Specify the order ID.

    Making an update, updates the status of the order
    */
    public Mono<ApiResponse<Boolean>> update(String id, UpdateOrderStatus status){

        return reactiveMongoTemplate.findById(id, Order.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Order Id not found")))
                .flatMap(order -> {
                    order.setOrderStatus(status.getStatus());

                    return reactiveMongoTemplate.save(order);
                })
                .then(Mono.just(ApiResponse.success(true, "Update sucess")))
                .onErrorResume( e-> Mono.just(ApiResponse.error("error can't proceed{}" + e.getMessage())));
    }
}
