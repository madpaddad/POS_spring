package com.example.demo.services;

import com.example.demo.dto.category.Create;
import com.example.demo.dto.order.ProductDTO;
import com.example.demo.helper.ApiResponse;
import com.example.demo.helper.MessageResponse;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    /**********************************************************************
     Get Category
     ***********************************************************************/

    public ResponseEntity<List<Category>> get() {

        List<Category> list;

        list = categoryRepository.findAll();

        if (list.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(list);
    }

    /**********************************************************************
     Create Category
     ***********************************************************************/

    public ResponseEntity<ApiResponse<Create>> create(Create create) {

        try {

            // Check if a category name exist before insert
            if(categoryRepository.existsByName(create.getName())){
                throw new IllegalArgumentException("ប្រភេទទិន្នន័យមាន");
            }

            Category save = categoryRepository.save(mapper.toEntity(create));

            ApiResponse<Create> response = ApiResponse.success(create, "ផលិតផលបង្កើតបានជោគជ័យ");
            return ResponseEntity.ok(response);

        } catch (Exception e){
//            logger.error(e);
            e.printStackTrace();
//            logger.error("Error found", e);
            ApiResponse<Create> response = ApiResponse.error("មិនអាចបង្កើតផលិតផលបាន", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**********************************************************************
     Update Product
     ***********************************************************************/

//    public ResponseEntity<ApiResponse<ProductDTO>> update(String id, ProductDTO product){
//
//        try {
//            if(id.isEmpty()){
//                throw new IllegalArgumentException("no ID found");
//            }
//            Product data = productRepository.findById(id).orElse(null);
//
//            if(data == null){
//                throw new IllegalArgumentException("data is null");
//            }
//
//            mapper.update(product, data);
//
//            Product savedProduct = productRepository.save(data);
//
//            ApiResponse<ProductDTO> response = ApiResponse.success(product, "មានក្នុងស្តុក");
//            return ResponseEntity.ok(response);
//        } catch (Exception e){
//
//            ApiResponse<ProductDTO> response = ApiResponse.error("គ្មានផលិតផលក្នុងស្តុក", e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//
//        }
//    }

    /**********************************************************************
     Delete Product
     ***********************************************************************/

//    public ResponseEntity<ApiResponse<String>> delete(String id){
//
//        try {
//
//            if (id.isEmpty()){
//                throw new IllegalArgumentException("no ID found");
//            }
//
//            Product data = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("data is null"));
//
//            productRepository.delete(data);
//
//            ApiResponse<String> response = ApiResponse.success(null, "លុបទិន្នន័យជោគជ័យ");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//
//            ApiResponse<String> response = ApiResponse.error("error", e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        }
//    }
}
