package com.example.demo.services;

import com.example.demo.config.ResourceNotFound;
import com.example.demo.dto.category.Create;
import com.example.demo.dto.category.DeleteCategoryDTO;
import com.example.demo.dto.category.UpdateCategoryDTO;
import com.example.demo.helper.ApiResponse;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import com.mongodb.client.result.UpdateResult;

import java.util.List;



@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;
    private final ProductRepository productRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private final WebClient webClient;

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    public CategoryService(CategoryRepository categoryRepository, ReactiveMongoTemplate reactiveMongoTemplate , CategoryMapper mapper, ProductRepository productRepository, ReactiveMongoTemplate reactiveMongoTemplate1, WebClient webClient) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.productRepository = productRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate1;
        this.webClient = webClient;
    }

    /**********************************************************************
     Get Category
     ***********************************************************************/

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

        return categoryRepository.save(mapper.toEntity(create))
                // 2. Decide what to do based on the existence check
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        // Category exists: Terminate the chain by throwing an error
                        // (or returning a Mono.error)
                        return Mono.error(new IllegalArgumentException("ប្រភេទទិន្នន័យមាន"));
                    }

                    // Category does not exist: Proceed with the WebClient call
                    log.info("Attempting to create a new bucket and category.");
                    log.info("Creating a category call{}", create);
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
    }




    /**********************************************************************
     Update
     ***********************************************************************/

//    public Mono<ResponseEntity<ApiResponse<UpdateCategoryDTO>>> update(String id, UpdateCategoryDTO updateCategoryDTO) {
//
//        try {
//
//            Category category = mongoTemplate.findOne(Query.query(Criteria.where("name").is(updateCategoryDTO.getName())), Category.class);
//
//            if(updateCategoryDTO.getName().isEmpty()){
//                throw new IllegalArgumentException("No Category Provided");
//            }
//
//            Boolean exists = categoryRepository.existsByName(updateCategoryDTO.getName()).blockOptional().orElseThrow(() ->
//                    new ResourceNotFound("Category Not found in data"));
//
//            // Update the bucket name:
//            Mono<UpdateCategoryDTO> bucket_update = updateToBucketService(updateCategoryDTO);
//
//
//            log.info("Status of updating youyr minio: {}", status);
//            // Query key category with name
//            Query query = new Query(Criteria.where("category").is(updateCategoryDTO.getName()));
//            // Set with new value
//            Update update = new Update().set("category", updateCategoryDTO.getNew_name());
//            // Update value in product class
//            mongoTemplate.updateMulti(query, update, Product.class);
//
//
//            // Update value in Category class
//            Category updatedCategory  = mongoTemplate.update(Category.class)
//                    .matching(Query.query(Criteria.where("name").is(category.getName())))
//                    .apply(new Update().set("name", updateCategoryDTO.getNew_name()))
//                    .withOptions(FindAndModifyOptions.options().returnNew(true))
//                    .findAndModifyValue();
//
//            log.info("Your category is updated: {}", updatedCategory.getName());
//
//            ApiResponse<UpdateCategoryDTO> response = ApiResponse.success(updateCategoryDTO, "ព័ត៌មានកែប្រែដោយជោគជ័យ");
//
//            return ResponseEntity.ok(status);
//        } catch (Exception e){
////
//            ApiResponse<UpdateCategoryDTO> response = ApiResponse.error("គ្មានផលិតផលក្នុងប្រភេទ");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
////
//        }
//    }


    public Mono<UpdateCategoryDTO> update(String id, UpdateCategoryDTO dto) {

        if (dto.getName() == null || dto.getName().isEmpty()) {
            return Mono.error(new IllegalArgumentException("No Category Provided"));
        }

        return updateToBucketService(dto)   // Mono<UpdateCategoryDTO>
                .flatMap(this::update_product)   // Mono<UpdateCategoryDTO>
                .flatMap(this::update_category); // Mono<UpdateCategoryDTO>
    }

    public Mono<UpdateCategoryDTO> update_product(UpdateCategoryDTO updateCategoryDTO){

        Query query = new Query(Criteria.where("category").is(updateCategoryDTO.getName()));
        Update update = new Update().set("category", updateCategoryDTO.getNew_name());

        return reactiveMongoTemplate.updateMulti(query, update, Product.class)
                .thenReturn(updateCategoryDTO);
    }

    public Mono<UpdateCategoryDTO> update_category(UpdateCategoryDTO updateCategoryDTO){
        return reactiveMongoTemplate.findAndModify(
                Query.query(Criteria.where("name").is(updateCategoryDTO.getName())),
                new Update().set("name", updateCategoryDTO.getNew_name()),
                FindAndModifyOptions.options().returnNew(true),
                Category.class
        )
                .switchIfEmpty(Mono.error(new RuntimeException("Category not found")))
                .thenReturn(updateCategoryDTO);
    }


    public Mono<UpdateCategoryDTO> updateToBucketService(UpdateCategoryDTO updateCategoryDTO) {
        return webClient.put()
                .uri("/api/fileService/bucket")
                .bodyValue(updateCategoryDTO)
                .retrieve()
                // Successful path returns Mono<ResponseEntity<Void>>
                .toBodilessEntity()
                // Map successful status to ResponseEntity<HttpStatus> (which is a form of Object)
                .map(response -> updateCategoryDTO )
                .onErrorMap(e -> new RuntimeException("Failed to update bucket", e));
    }
    /**********************************************************************
     Delete
     ***********************************************************************/

    public ResponseEntity<ApiResponse<String>> delete(DeleteCategoryDTO deleteCategoryDTO){

        try {

            if (deleteCategoryDTO.getName().isEmpty()){
                throw new IllegalArgumentException("no name provided");
            }
;

            Query query = new Query(Criteria.where("name").is(deleteCategoryDTO.getName()));
            Category data = mongoTemplate.findOne(query, Category.class);

            // FInd the bucket in fileservice then remove it:


            if(data == null){
                throw new ObjectNotFoundException("ទិន្នន័យមិនមាន");
            }

            Query product = new Query(Criteria.where("category").is(deleteCategoryDTO.getName()));
            Boolean exist = mongoTemplate.exists(product, Product.class);

            if(exist){
                throw new IllegalArgumentException("ទិន្នន័យនេះមានក្នុងស្តុក");
            }

            mongoTemplate.remove(data);

            ApiResponse<String> response = ApiResponse.success(null, "លុបទិន្នន័យជោគជ័យ");
            return ResponseEntity.ok(response);
        } catch (Exception e) {

            ApiResponse<String> response = ApiResponse.error("error");
            log.info("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
