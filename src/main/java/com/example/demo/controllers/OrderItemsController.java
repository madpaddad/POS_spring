package com.example.demo.controllers;

import com.example.demo.dto.order.UpdateOrderItemDTO;
import com.example.demo.helper.ApiResponse;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.services.OrderItemsService;
import com.example.demo.services.OrderService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemsController {

    private final OrderItemsService orderItemsService;

    public OrderItemsController(OrderItemsService orderItemsService) {
        this.orderItemsService = orderItemsService;
    }

    /*
    * @ params id        : specify the TableOrder. Because a table can have more than 2 difference guests

    * @ body List<Product> and its quantity
    */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<Product>> create(@RequestParam() String id,
                                                     @RequestBody() UpdateOrderItemDTO items){
        return orderItemsService.create(id, items);
    }

    @PutMapping()
    public Mono<ApiResponse<OrderItem>> update(
            @RequestParam() String id,
            @RequestBody() OrderItem item
    ){
        return orderItemsService.update(id, item);
    }
}
