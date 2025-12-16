package com.example.demo.services;

import com.example.demo.dto.category.Create;
import com.example.demo.dto.order.ProductDTO;
import com.example.demo.helper.ApiResponse;
import com.example.demo.helper.MessageResponse;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;
    @Autowired
    private final WebClient webClient;

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper mapper, WebClient webClient) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.webClient = webClient;
    }

    /**********************************************************************
     Get Category
     ***********************************************************************/

//    public ResponseEntity<List<Category>> get() {
//
//        List<Category> list;
//
//        Flux<Category> list = categoryRepository.findAll();
//        list.
//        if (list.bl) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok(list);
//    }
    public Mono<ResponseEntity<List<Category>>> get() {

        // Call the service method, which returns Flux<Category>
        Flux<Category> categoriesFlux = categoryRepository.findAll();

        return categoriesFlux
                // 2. Collect all items emitted by the Flux into a single List.
                // This transforms Flux<Category> into Mono<List<Category>>.
                .collectList()

                // 3. Decide the final response based on the Mono<List<Category>> result.
                // 'map' is used because we are changing the Mono's content (List<Category> to ResponseEntity).
                .map(list -> {
                    if (list.isEmpty()) {
                        // Case 1: The list is empty. Return 404 Not Found or 204 No Content.
                        // 404 is technically for a non-existent resource, but 204/200 are more common for empty lists.
                        return ResponseEntity.noContent().build(); // HTTP 204 No Content
                        // return ResponseEntity.ok(list); // HTTP 200 OK with empty list [] (Also common)
                    } else {
                        // Case 2: Categories were found. Return 200 OK with the list.
                        return ResponseEntity.ok(list); // HTTP 200 OK with the list
                    }
                });
                // 4. Handle the case where the Flux is truly empty (e.g., no emissions at all).
                // This is primarily for error handling, but we ensure a Mono<ResponseEntity> is always returned.
//                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    /**********************************************************************
     Create Category
     ***********************************************************************/

    // Method signature MUST change to return a Mono
    public Mono<ResponseEntity<ApiResponse>> create(Create create) {
        log.info("We are going into the servicee");
        // 1. Check if category exists (Mono<Boolean>)
        Mono<Boolean> existsMono = categoryRepository.existsByName(create.getName());

        return existsMono
                // 2. Decide what to do based on the existence check
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        // Category exists: Terminate the chain by throwing an error
                        // (or returning a Mono.error)
                        return Mono.error(new IllegalArgumentException("ប្រភេទទិន្នន័យមាន"));
                    }

                    // Category does not exist: Proceed with the WebClient call
                    log.info("Attempting to create a new bucket and category.");

                    // 3. Call the external service (Mono<ResponseEntity<Object>>)
                    // We use .flatMap() to wait for the web service call to complete
                    log.info("jam mer vea return ey {}", postToBucketService(create));
                    return postToBucketService(create);
                });
    }


    // Method signature change: Mono<ResponseEntity<Object>>
    public Mono<ResponseEntity<ApiResponse>> postToBucketService(Create create) {
        return webClient.post()
                .uri("/api/fileService/bucket")
                .bodyValue(create)
                .retrieve()
                // Successful path returns Mono<ResponseEntity<Void>>
                .toBodilessEntity()
                // Map successful status to ResponseEntity<HttpStatus> (which is a form of Object)
                .map(response -> ResponseEntity.<Object>status(response.getStatusCode()).build());
//                .onError();
                // Handle errors
//                .onErrorResume(WebClientResponseException.class, e -> {
//                    // Error path returns Mono<ResponseEntity<Object>>
//
//                    // Log the error
//                    System.err.println("Error posting to bucket service: " + e.getRawStatusCode() + " - " + e.getResponseBodyAsString());
//
//                    // Option A: Return a structured error response
//                    // Create a custom error body (e.g., a Map or a POJO)
//                    Map<String, String> errorBody = Map.of(
//                            "error", "External Service Failure",
//                            "message", e.getMessage()
//                    );
//
//                    return Mono.just(
//                            ResponseEntity.status(e.getStatusCode())
//                                    .body(errorBody) // Body is Map<String, String>, which is Object
//                    );
//
//                    // Option B: Re-throw the exception to be handled by a ControllerAdvice
//                    // return Mono.error(e);
//                });
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
