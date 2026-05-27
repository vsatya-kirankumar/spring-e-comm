package com.ecommerce.project.controller;

import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/admin/categories")
    public ResponseEntity<List<CategoryDTO>> addNewCategory(@RequestBody List<CategoryDTO> categoryDTOs) {
        List<CategoryDTO> categories = categoryDTOs.stream().map(categoryDTO -> {
            CategoryDTO newCategory = categoryService.addCategory(categoryDTO);

            return newCategory;
        }).toList();

        return new ResponseEntity<List<CategoryDTO>>(categories,HttpStatus.CREATED);
    }
}