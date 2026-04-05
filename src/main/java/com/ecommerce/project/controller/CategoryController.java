package com.ecommerce.project.controller;

import com.ecommerce.project.DTO.CategoryResponse;
import com.ecommerce.project.exception.ValidationSequence;
import com.ecommerce.project.model.CategoryModel;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategories() {
        CategoryResponse allCategories = categoryService.getCategories();
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<String> addCategory(@Validated(ValidationSequence.class) @Valid @RequestBody CategoryModel category) {
        categoryService.addCategory(category);
        return new ResponseEntity<>("Category added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> removeCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category remove successfully", HttpStatus.ACCEPTED);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryModel> updateCategory(@RequestBody CategoryModel category, @PathVariable Long categoryId) {
        CategoryModel updatedCategory = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }
}
