package com.ecommerce.project.service;

import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.dto.CategoryResponse;
import com.ecommerce.project.model.CategoryModel;

public interface CategoryService {
    CategoryResponse getAllCategories();

    String addCategory(CategoryModel category);

    String deleteCategory(Long categoryId);

    CategoryModel updateCategory(CategoryModel category, Long categoryId);
}