package com.example.demo.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

// model
import com.example.model.Product;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    
    @GetMapping()
    @ResponseBody
    public  ResponseEntity<List<Product>> getProduct(){

    }
}
