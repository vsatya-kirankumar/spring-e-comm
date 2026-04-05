package com.ecommerce.project.service;

import com.ecommerce.project.DTO.CategoryResponse;
import com.ecommerce.project.model.CategoryModel;

import java.util.List;

public interface CategoryService {
    CategoryResponse getCategories();
    String addCategory(CategoryModel category);
    String deleteCategory(Long categoryId);
    CategoryModel updateCategory(CategoryModel category, Long categoryId);
}
