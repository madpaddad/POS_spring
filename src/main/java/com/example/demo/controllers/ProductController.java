package com.example.demo.controllers;


import com.example.demo.dto.order.ProductDTO;
import com.example.demo.helper.ApiResponse;
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
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping()
    @ResponseBody
    public  ResponseEntity<List<Product>> getProduct(@RequestParam(required = false) String category_id){
        return this.productService.get(category_id);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ApiResponse<Product>> createStudent(@RequestBody Product product) {
//        Product savedproduct = product;
//        ApiResponse<Product> apiResponse = ApiResponse.success(product, "hello");
//        return ResponseEntity.ok(apiResponse);
        return this.productService.create(product);
    }
}
