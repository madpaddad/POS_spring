package com.example.demo.controllers;


import com.example.demo.dto.order.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.demo.services.ProductService;
// model
import com.example.demo.model.Product;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {


    private final ProductService productService;
//    private final OrderRepository orderRepository;
    public ProductController(ProductService productService) {
        this.productService = productService;
//        this.orderRepository = orderRepository;
    }


    @GetMapping()
    @ResponseBody
    public  ResponseEntity<List<Product>> getProduct(@RequestParam(required = false) String category_id){
        return this.productService.get(category_id);
    }
}
