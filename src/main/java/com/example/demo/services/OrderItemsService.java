package com.example.demo.services;

import com.example.demo.dto.order.UpdateOrderItemDTO;
import com.example.demo.helper.ApiResponse;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.model.TableOrder;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.InvalidNameException;
import java.util.List;
import java.util.Map;

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
    public Mono<ApiResponse<Product>> create(String id, UpdateOrderItemDTO orderItems) {
        Mono<TableOrder> order = reactiveMongoTemplate.findById(id, TableOrder.class);

        return reactiveMongoTemplate.findById(id, TableOrder.class)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not found")))
                .then(Mono.just(orderItems.getProduct_id()))
                .flatMap(product_id -> reactiveMongoTemplate.findById(product_id, Product.class)
                )
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not exist")))
                .flatMap(product -> Mono.just(ApiResponse.success(product, "hello")));
//                t_order -> {Mono.map(
//                            orderItems -> reactiveMongoTemplate
//                                    .findById(orderItems.getProduct_id(), Product.class)
//                                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")))
//                                    .map(product -> {
//                                        double price = product.getPrice();
//                                        double total = price * item.getQuantity();
//                                        log.info("Price from product is, {} {}", price, total);
//
//                                        // Checking if the product has its variant
//                                        if(product.isHas_subproduct()){
//                                            Map<String, Double> variants = product.getsub_product();
//
//                                            if(!variants.containsKey(orderItems.getVariant())){
//                                                return Mono.error(new RuntimeException("Invalid variant"));
//                                            }
//
//                                            price = variants.getprice();
//                                        }
//
//                                        item.setTotal(total);
//                                        item.setOrder_id(t_order.getOrder());
//
//
//
//                                        return item;
//                                    })
//                                    .flatMap(reactiveMongoTemplate::save));
//
//                    return items.collectList();
//                })
//                .map( item -> {
//                            return ApiResponse.success(orderItems, "saved");
//                }
//                )
//                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found")))
//                .onErrorResume(e -> Mono.just(ApiResponse.error("failed " + e.getMessage() )));
    }

    public Mono<ApiResponse<OrderItem>> update(String id, OrderItem orderItem){

        Query query = new Query(Criteria.where("$id").is(id));
        Update update = new Update();

        if(orderItem != null && orderItem.getProduct_id()!= null){
            update.set("product_id", orderItem.getProduct_id());
        }

        if(orderItem != null && orderItem.getQuantity() > 0){
            update.set("quantity", orderItem.getQuantity());
        }

        return reactiveMongoTemplate.updateFirst(
                    query,
                    update,
                    OrderItem.class
                )
                .map(updated -> {
                    return ApiResponse.success(orderItem, "ផ្លាស់ប្ដូរជោគជ័យ");
                })
                .onErrorResume(
                        throwable -> Mono.just(ApiResponse.error(throwable.getMessage()))
                );
    }

}
