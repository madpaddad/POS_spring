package com.example.demo.services;

import com.example.demo.dto.order.ProductDTO;
import com.example.demo.helper.ApiResponse;
import com.example.demo.helper.CustomerMapper;
import com.example.demo.helper.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;

import com.example.demo.share.MongoQuery;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import java.util.List;
import java.util.Optional;;

@Service
public class ProductService {

    public Logger logger;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
//    private final MongoQuery mongoQuery;
    private final ProductMapper mapper;
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
//        this.mongoQuery = mongoQuery;
        this.mapper = mapper;
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
            ApiResponse<Product> response = ApiResponse.error("មិនអាចបង្កើតផលិតផលបាន", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**********************************************************************
     Update Product
     ***********************************************************************/

    public ResponseEntity<ApiResponse<ProductDTO>> update(String id, ProductDTO product){

        try {
            if(id.isEmpty()){
                throw new IllegalArgumentException("no ID found");
            }
            Product data = productRepository.findById(id).orElse(null);

            if(data == null){
                throw new IllegalArgumentException("data is null");
            }

            mapper.update(product, data);

            Product savedProduct = productRepository.save(data);

            ApiResponse<ProductDTO> response = ApiResponse.success(product, "មានក្នុងស្តុក");
            return ResponseEntity.ok(response);
        } catch (Exception e){

            ApiResponse<ProductDTO> response = ApiResponse.error("គ្មានផលិតផលក្នុងស្តុក", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }
    }

    /**********************************************************************
     Delete Product
     ***********************************************************************/

    public ResponseEntity<ApiResponse<String>> delete(String id){

        try {

            if (id.isEmpty()){
                throw new IllegalArgumentException("no ID found");
            }

            Product data = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("data is null"));

            productRepository.delete(data);

            ApiResponse<String> response = ApiResponse.success(null, "លុបទិន្នន័យជោគជ័យ");
            return ResponseEntity.ok(response);
        } catch (Exception e) {

            ApiResponse<String> response = ApiResponse.error("error", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}

