package com.example.demo.services;

import com.example.demo.helper.ApiResponse;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import java.util.List;;

@Service
public class ProductService {

    public Logger logger;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**********************************************************************
    Get Product
    ***********************************************************************/

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

    /**********************************************************************
     Create Product
     ***********************************************************************/

    public ResponseEntity<ApiResponse<Product>> create(Product product) {

        try {

            // Check if a category name exist before insert
            if(!categoryRepository.existsByName(product.getCategory())){
                logger.info(product.getCategory());
                throw new IllegalArgumentException("មិនមានប្រភេទទិន្នន័យ");
            }

            Product savedproduct = productRepository.save(product);

            ApiResponse<Product> response = ApiResponse.success(savedproduct, "ផលិតផលបង្កើតបានជោគជ័យ");
            return ResponseEntity.ok(response);

        } catch (Exception e){
//            logger.error(e);
            e.printStackTrace();
            logger.error("Error found", e);
            ApiResponse<Product> response = ApiResponse.error("មិនអាចបង្កើតផលិតផលបាន");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }
}

