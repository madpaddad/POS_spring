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
    public  ResponseEntity<List<Product>> get(@RequestParam(required = false) String category_id){
        return this.productService.get(category_id);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ApiResponse<Product>> create(@RequestBody Product product) {
        return this.productService.create(product);
    }

    @PutMapping()
    @ResponseBody
    public ResponseEntity<ApiResponse<ProductDTO>> update(@RequestParam(required = true) String id,@RequestBody ProductDTO product){
        return this.productService.update(id, product);
    }

    @DeleteMapping()
    @ResponseBody
    public ResponseEntity<ApiResponse<String>> delete(@RequestParam(required = true) String id){
        return this.productService.delete(id);
    }
}
