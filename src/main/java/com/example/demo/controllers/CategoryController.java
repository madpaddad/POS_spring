package com.example.demo.controllers;

import com.example.demo.dto.category.Create;
import com.example.demo.dto.category.UpdateCategoryDTO;
import com.example.demo.helper.ApiResponse;
import com.example.demo.model.Category;
import com.example.demo.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.reflections.Reflections.log;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**********************************************************************
     Get Product
     ***********************************************************************/
    @GetMapping()
    @ResponseBody
    public Mono<ResponseEntity<List<Category>>> get(){
        return this.categoryService.get();
    }

    @PostMapping()
    @ResponseBody
    public Mono<ResponseEntity<ApiResponse>> create(@RequestBody(required=true) Create create) {
//        log.info("Controller buycket hit");
        return this.categoryService.create(create);

//        return Mono.just(
//                ResponseEntity.ok("Hello")
//        );
    }

    @PutMapping()
    @ResponseBody
    public ResponseEntity<ApiResponse<UpdateCategoryDTO>> update(@RequestParam(required = false) String id, @RequestBody UpdateCategoryDTO updateCategoryDTO){
        return this.categoryService.update(id, updateCategoryDTO);
    }
//
//    @DeleteMapping()
//    @ResponseBody
//    public ResponseEntity<ApiResponse<String>> delete(@RequestParam(required = true) String id){
//        return this.categoryService.delete(id);
//    }
}
