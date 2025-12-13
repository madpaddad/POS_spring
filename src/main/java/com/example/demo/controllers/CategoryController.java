package com.example.demo.controllers;

import com.example.demo.dto.category.Create;
import com.example.demo.helper.ApiResponse;
import com.example.demo.model.Category;
import com.example.demo.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/WASSUP")
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
    public ResponseEntity<List<Category>> get(){
        return this.categoryService.get();
    }

    @PostMapping()
    @ResponseBody
    public ResponseEntity<ApiResponse<Create>> create(@RequestBody(required=true) Create create) {
        return this.categoryService.create(create);
    }

//    @PutMapping()
//    @ResponseBody
//    public ResponseEntity<ApiResponse<ProductDTO>> update(@RequestParam(required = true) String id,@RequestBody ProductDTO product){
//        return this.categoryService.update(id, product);
//    }
//
//    @DeleteMapping()
//    @ResponseBody
//    public ResponseEntity<ApiResponse<String>> delete(@RequestParam(required = true) String id){
//        return this.categoryService.delete(id);
//    }
}
