package com.ecommerce.project.controller;

import com.ecommerce.project.model.CategoryModel;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<List<CategoryModel>> getCategories() {
        List<CategoryModel> allCategories = categoryService.getCategories();
        try{
            if(!allCategories.isEmpty()) {
                return new ResponseEntity<>(allCategories, HttpStatus.OK);
            }
        }catch (ResponseStatusException e) {
            return new ResponseEntity<>(allCategories, e.getStatusCode());
        }
        return new ResponseEntity<>(allCategories, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryModel category) {
        try{
            String status = categoryService.addCategory(category);
            return new ResponseEntity<>(status, HttpStatus.CREATED);
        }catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> removeCategory(@PathVariable Long categoryId) {
        try {
            String status = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.ACCEPTED);
        }catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryModel> updateCategory(@RequestBody CategoryModel category,@PathVariable Long categoryId) {
        CategoryModel updatedCategory = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }
}
