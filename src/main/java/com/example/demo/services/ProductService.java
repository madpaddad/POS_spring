package com.example.demo.services;

import com.example.demo.dto.file.Create;
import com.example.demo.dto.order.ProductDTO;
import com.example.demo.dto.product.ProductFile;
import com.example.demo.helper.ApiResponse;
import com.example.demo.helper.CustomerMapper;
import com.example.demo.helper.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;

import com.example.demo.share.MongoQuery;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import static org.reflections.Reflections.log;

@Service
public class ProductService {

    public Logger logger;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;
    @Autowired
    private final WebClient webclient;
    private final ProductMapper mapper;


    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, FileService fileService, WebClient webclient, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
        this.webclient = webclient;
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

    public ResponseEntity<ApiResponse<ProductDTO>> create(ProductDTO product, MultipartFile file) {

        try {

            // Check if a category name exist before insert
            Boolean exists = categoryRepository.existsByName(product.getCategory()).block();
            if (exists == null || !exists) {
                throw new IllegalArgumentException("មិនមានប្រភេទទិន្នន័យ");
            }


            if (product.ishas_subproducts()) {
                if (product.getSub_products() == null){
                    throw new IllegalArgumentException("មិនមានទិន្នន័យគ្រប់គ្រាន់ដើម្បីបញ្ចូល");
                }
            }


            log.info("file: {}", file);
            if (file != null && !file.isEmpty()) {

                MultipartBodyBuilder builder = new MultipartBodyBuilder();
                Create prod = new Create(product.getCategory(), product.getName());
                String path = fileService.callHi(prod, file);

                log.info("path{}", path);
            }



            // Check for sub product and boolean

            Product savedproduct = productRepository.save(mapper.toEntity(product));

            ApiResponse<ProductDTO> response = ApiResponse.success(product, "ផលិតផលបង្កើតបានជោគជ័យ");
            return ResponseEntity.ok(response);

        } catch (Exception e){
//            logger.error(e);
            e.printStackTrace();
//            logger.error("Error found", e);
            ApiResponse<ProductDTO> response = ApiResponse.error("មិនអាចបង្កើតផលិតផលបាន");
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

            ApiResponse<ProductDTO> response = ApiResponse.error("គ្មានផលិតផលក្នុងស្តុក");
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

            ApiResponse<String> response = ApiResponse.error("error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}

