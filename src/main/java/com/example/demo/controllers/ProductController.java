package com.example.demo.controllers;


import com.example.demo.dto.order.ProductDTO;
import com.example.demo.helper.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.demo.services.ProductService;
// model
import com.example.demo.model.Product;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.reflections.Reflections.log;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Mono<ApiResponse<ProductDTO>> create(
//            public ResponseEntity<String> create (
            @RequestPart(value = "product", required = true) ProductDTO product,
            @RequestPart(value = "file", required = true) MultipartFile file) {
        return this.productService.create(product, file);
    }

//    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ResponseBody
//    public Mono<ApiResponse<ProductDTO>> update(
//            @RequestParam(required = true) String id,
//            @RequestPart(value = "product", required = true) ProductDTO product,
//            @RequestPart(value = "image", required = false) MultipartFile file){
//
//        log.info("Product request{}", product);
//        log.info("File request{}", file);
//        return this.productService.update(id, product, file);
//    }
    @PutMapping(value = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Mono<ApiResponse<ProductDTO>> update(
            @RequestParam String id,
            @RequestPart("product") ProductDTO product,
            @RequestPart(value = "image", required = false) MultipartFile file
    ) {
        log.info("processing request {}", product);
        return this.productService.update(id, product, file);
    }


    @DeleteMapping()
    @ResponseBody
    public ResponseEntity<ApiResponse<String>> delete(@RequestParam(required = true) String id){
        return this.productService.delete(id);
    }
}
