package com.example.demo.services;

import com.example.demo.dto.file.Create;
import com.example.demo.dto.file.UpdateFileDTO;
import com.example.demo.dto.order.ProductDTO;
import com.example.demo.helper.ApiResponse;
import com.example.demo.helper.ProductMapper;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage;
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
    private final WebClient webClient;
    private final ProductMapper mapper;
    private final ReactiveMongoTemplate reactiveMongoTemplate;


    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, FileService fileService, WebClient webclient, ProductMapper mapper, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
        this.webClient = webclient;
        this.mapper = mapper;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    /**********************************************************************
     Get Product
     ***********************************************************************/
    public ResponseEntity<List<Product>> get(String category_id) {

        List<Product> productList;

        if (category_id == null) {
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

    public Mono<ApiResponse<ProductDTO>> create(ProductDTO productDTO, MultipartFile file) {

        if (productDTO.ishas_subproducts()) {
            if (productDTO.getSub_products() == null) {
                throw new IllegalArgumentException("មិនមានទិន្នន័យគ្រប់គ្រាន់ដើម្បីបញ្ចូល");
            }
        }

        Query query = new Query(Criteria.where("name").is(productDTO.getCategory()));
        Mono<Boolean> exist = reactiveMongoTemplate.exists(query, Category.class);

        return exist.flatMap(e -> {
            if (!e) {
                return Mono.error(new IllegalArgumentException("Category does not exist"));
            }

            Product product = mapper.toEntity(productDTO);

            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            Create prod = new Create(productDTO.getCategory(), productDTO.getName());

            log.info("{}", file.isEmpty());
            Mono<String> pathMono = file.isEmpty() ? Mono.just("") : fileService.create(prod, file);
            return pathMono
                    .map(path -> {

                        /*
                        If path exists it will return back as a path:
                        Example:
                        {
                            "path": "fastfood/Product5"
                        }
                        */
                                if (path != null && !path.isBlank()) {
                                    product.setPath(path);
                                }
                                return product;
                            }
                    )
                    .flatMap(d -> reactiveMongoTemplate.save(d))
                    .map(d -> ApiResponse.success(productDTO, "ទិន្នន័យបានបង្កើត"))
                    .onErrorResume(throwable -> Mono.just(ApiResponse.error(throwable.getMessage())));
        });

    }

    /**********************************************************************
     Update Product:
     ** MINIO DOES NOT ALLOW BOTH OBJECT AND BUCKET NAME CHANGING **

     - Updating Image and Name: Find the old image, remove, and replace with the new name and new path;
     - Updating Image : Find the old image, remove, but keep the same name
     - Updating Name  : Just update the name, path keep the same
     ***********************************************************************/

    public Mono<ApiResponse<ProductDTO>> update(String id, ProductDTO productDTO, MultipartFile file) {

        Mono<Product> product = reactiveMongoTemplate.findById(id, Product.class);


        // Get the product and update the path with the new name
        Mono<String> filepath = Mono.justOrEmpty(file)
                .filter(f -> !f.isEmpty())
                .zipWith(product)
                .flatMap(tuple -> {
                    log.info("Tuple name: {}", tuple.getT2().getName());
                    UpdateFileDTO updateFileDTO = new UpdateFileDTO(tuple.getT2().getName(), productDTO.getName());

                    return fileService.update(updateFileDTO, tuple.getT1());
                });


        log.info("{}", productDTO.getName());
        return product
                .flatMap(p -> {

                    Query query = new Query(Criteria.where("_id").is(id));
                    Update update = new Update();

                    return filepath
                            .doOnNext(path -> update.set("path", filepath))
                            .then(Mono.defer(() -> {
                                if (productDTO.getName() != null) {
                                    log.info("name{}", productDTO.getName());
                                    update.set("name", productDTO.getName());
                                }
                                if (productDTO.getPrice() != null) {
                                    update.set("price", productDTO.getPrice());
                                }
                                if (productDTO.getCategory() != null) {
                                    update.set("category", productDTO.getCategory());
                                }
                                if (productDTO.getIs_available() != null) {
                                    update.set("category", productDTO.getIs_available());
                                }

                                return reactiveMongoTemplate.updateFirst(query, update, Product.class);
                            }))
                            .map(r ->
                            {
                                return ApiResponse.success(productDTO, "ព៏ត៌មានកែប្រែ");
                            })
                            .onErrorReturn(ApiResponse.error("មានបញ្ហាក្នុងការកែប្រែ"));
                });
    }

    /**********************************************************************
     Delete Product
     ***********************************************************************/

    public ResponseEntity<ApiResponse<String>> delete(String id) {

        try {

            if (id.isEmpty()) {
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



