package com.example.demo.services;

import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.Table;
import com.example.demo.model.TableOrder;
import com.example.demo.repository.OrderRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.dto.order.orderDTO;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    public OrderService(OrderRepository orderRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.orderRepository = orderRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    public ResponseEntity<orderDTO> get(String id) {

        System.out.println("Requested service order ID: " + id);
        orderDTO orderList = orderRepository.findOrderById(id);

        if (orderList == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(orderList);

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
                    TableOrder tableorder = new TableOrder(id, table);
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
}
