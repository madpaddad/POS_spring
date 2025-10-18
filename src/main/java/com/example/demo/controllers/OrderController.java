package com.example.demo.controllers;
import com.example.demo.dto.orderDTO;

import com.example.demo.repository.OrderRepository;
import com.example.demo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @GetMapping()
    @ResponseBody
    public ResponseEntity<orderDTO> get(@RequestParam(required = true) String id) {
        System.out.println("Requested order ID: " + id);
        return this.orderService.get(id);
    }
}
