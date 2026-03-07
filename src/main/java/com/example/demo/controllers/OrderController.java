package com.example.demo.controllers;
import com.example.demo.dto.order.UpdateOrderStatus;
import com.example.demo.dto.order.orderDTO;

import com.example.demo.helper.ApiResponse;
import com.example.demo.model.OrderStatus;
import com.example.demo.repository.OrderRepository;
import com.example.demo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.bson.Document;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

//    @PostMapping()
//    public PostEntity

    @GetMapping()
    @ResponseBody
    public Mono<ApiResponse<List<Document>>> get(@RequestParam(required = false) String id) {
        return this.orderService.get(id);
    }

    @PostMapping()
    @ResponseBody
    public Mono<Boolean> post(@RequestParam(required = true) String table){
        return this.orderService.create(table);
    }

    @PutMapping()
    @ResponseBody
    public Mono<ApiResponse<Boolean>> update(
            @RequestParam String id,
            @RequestBody UpdateOrderStatus status
    ){
        return this.orderService.update(id, status);
    }
}
