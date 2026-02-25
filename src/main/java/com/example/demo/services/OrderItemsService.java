package com.example.demo.services;

import com.example.demo.helper.ApiResponse;
import com.example.demo.model.OrderItem;
import com.example.demo.model.TableOrder;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.naming.InvalidNameException;
import java.util.List;

import static org.reflections.Reflections.log;

@Service
public class OrderItemsService {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public OrderItemsService(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }


    /*
    - Check for if the TableOrder exist (id) to reference if there is an order from that
    - Create an orderItems to the table and reference to the order
     */
    public Mono<ApiResponse<List<OrderItem>>> create(String id, List<OrderItem> orderItems) {
        Mono<TableOrder> order = reactiveMongoTemplate.findById(id, TableOrder.class);
        return order.flatMap(
                t_order -> {

                    orderItems.forEach(item -> {
                        item.setOrder_id(t_order.getOrder());
                    });
                    return reactiveMongoTemplate.insertAll(orderItems).collectList();
                })
                .map( item -> {
                            return ApiResponse.success(orderItems, "saved");
                }
                )
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found")))
                .onErrorResume(e -> Mono.just(ApiResponse.error("failed " + e.getMessage() )));
    }
//                .flatMap(tableOrder -> {

//                    orderItems.forEach(item ->
//                            {
//                                item.setId(null);
//                                item.setOrder_id(tableOrder.getOrder());
//                                log.info("each item is setting{}: ", item);
//                            }
//                    );
//
//                    return Mono.just(ApiResponse.success(orderItems, "hello"));
//                            reactiveMongoTemplate.insertAll(orderItems)
//                            .collectList()
//                            .map(savedItem -> ApiResponse.success(savedItem, "ជោគជ័យ"));
//                })
//                .doOnError(e -> log.error("Error creating order items: ", e)) // See the real error!
//                .onErrorResume(e -> Mono.just(ApiResponse.error("Failed: " + e.getMessage())));
//    }

}
