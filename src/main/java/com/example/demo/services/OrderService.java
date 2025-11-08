package com.example.demo.services;

import com.example.demo.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.dto.order.orderDTO;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ResponseEntity<orderDTO> get(String id) {

        System.out.println("Requested service order ID: " + id);
        orderDTO orderList = orderRepository.findOrderById(id);

        if (orderList == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(orderList);

    }
}
