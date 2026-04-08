package com.ecommerce.project.controller;

import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.dto.CategoryResponse;
import com.ecommerce.project.exception.ValidationSequence;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "pageNumber") Integer pageNumber,
                                                             @RequestParam(name = "pageSize") Integer pageSize) {
        CategoryResponse allCategories = categoryService.getAllCategories(pageNumber, pageSize);
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> addCategory(@Validated(ValidationSequence.class) @Valid @RequestBody CategoryDTO category) {
        CategoryDTO savedCategory = categoryService.addCategory(category);
        return new ResponseEntity<CategoryDTO>(savedCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> removeCategory(@PathVariable Long categoryId) {
        CategoryDTO updatedCategory = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.ACCEPTED);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Validated(ValidationSequence.class) @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId) {
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }
}