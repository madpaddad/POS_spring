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
import org.springframework.web.ErrorResponseException;
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
    public Mono<ApiResponse<UpdateOrderItemDTO>> create(String id, UpdateOrderItemDTO orderItems) {


        return reactiveMongoTemplate.findOne(new Query(Criteria.where("order").is(id)), TableOrder.class)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Not found")))
                .flatMap(tableOrder -> {

                    return reactiveMongoTemplate.findById(orderItems.getProduct_id(), Product.class)
                            .switchIfEmpty(Mono.error(new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Product not exist")))
                            .flatMap(product -> {

                                log.info("Your product is here{}", product.getsub_product());
                                double price;

                                if (product.isHasSubproduct()) {
                                    Map<String, Integer> variants = product.getsub_product();

                                    if (!variants.containsKey(orderItems.getVariant())) {
                                        return Mono.just(ApiResponse.error("No variant product found"));
                                    }

                                    price = variants.get(orderItems.getVariant());
                                } else {
                                    price = product.getPrice();
                                }

                                double total = price * orderItems.getQuantity();

                                OrderItem orderitem = new OrderItem();
                                orderitem.setProduct_id(product.getId());
                                orderitem.setTotal(total);
                                orderitem.setQuantity(orderItems.getQuantity());
                                orderitem.setOrder_id(tableOrder.getOrder());

                                return reactiveMongoTemplate.save(orderitem)
                                        .map(saved -> ApiResponse.success(orderItems, "created to cart"));
                            });
                });
    }



    /* Update an Order item will change each field of the item order:
    @ param product : if a product is changed
    @ param quantity: if a quantity is added up

    ** Be careful in changing the total price as changing product could change the price and total of it
    */
    public Mono<ApiResponse<UpdateOrderItemDTO>> update(String id, UpdateOrderItemDTO updateOrderItemDTO){

        log.info("you are updating orderitem id: {}", updateOrderItemDTO.getProduct_id());

        Mono<Product> productMono;

        if (updateOrderItemDTO.getProduct_id() != null) {
            productMono = reactiveMongoTemplate
                    .findById(updateOrderItemDTO.getProduct_id(), Product.class);

        } else {

            // Use product from existing order item
            productMono = reactiveMongoTemplate.findById(id, OrderItem.class)
                    .switchIfEmpty(Mono.error(new RuntimeException("Order item not found")))
                    .flatMap(orderItem ->
                            reactiveMongoTemplate.findById(orderItem.getProduct_id(), Product.class)
                    );
        }

        return productMono
                .map(Product::getPrice)
                .map( price -> {

                    double total = price * updateOrderItemDTO.getQuantity();
                    log.info("updating the update of the item");

                    Update update = new Update();

                    update.set("total", total);

                    if(updateOrderItemDTO.getProduct_id() != null){
                        update.set("product_id", updateOrderItemDTO.getProduct_id());
                    }

                    if(updateOrderItemDTO.getQuantity() > 0 && updateOrderItemDTO.getQuantity() != null){
                        update.set("quantity", updateOrderItemDTO.getQuantity());
                    }

                    return update;
                })
                .flatMap(upd ->
                {
                    log.info(" I am updating {}", upd);

                    Query query = new Query(Criteria.where("_id").is(id));
                    log.info("Query value is{}", query);
                    return reactiveMongoTemplate.updateFirst(query, upd, OrderItem.class);
                })
                .map(result -> ApiResponse.success(updateOrderItemDTO, "update completed"))
                .onErrorResume(e -> {
                            if (e instanceof ErrorResponseException ex) {
                                return Mono.just(ApiResponse.error("Update Failed: " + ex.getMessage()));
                            }
                            return Mono.just(ApiResponse.error("Update Failed: " + e.getMessage()));
                });
    }

    /*
    @param id: find order id to delete
    */
    public Mono<ApiResponse<Boolean>> delete(String id){
        return reactiveMongoTemplate.findById(id, OrderItem.class)
                .switchIfEmpty(Mono.error(new ErrorResponseException(HttpStatus.NOT_FOUND)))
                .flatMap(reactiveMongoTemplate::remove)
                .flatMap(reactiveMongoTemplate::save)
                .thenReturn(ApiResponse.success(true, "deleted successfully"));
    }
}
