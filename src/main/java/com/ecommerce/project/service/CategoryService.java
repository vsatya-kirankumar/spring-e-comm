package com.ecommerce.project.service;

import com.ecommerce.project.model.CategoryModel;

import java.util.List;

public interface CategoryService {
    List<CategoryModel> getCategories();
    String addCategory(CategoryModel category);
    String deleteCategory(Long categoryId);
    CategoryModel updateCategory(CategoryModel category, Long categoryId);
}
