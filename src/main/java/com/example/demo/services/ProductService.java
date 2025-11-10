package com.example.demo.services;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ResponseEntity<List<Product>> get(String category_id) {

        List<Product> productList;

        if(category_id == null){
            productList = productRepository.findAll();
        } else {
            productList = productRepository.findByCategoryIdCustom(category_id);
        }

        if (productList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productList);
    }
}

