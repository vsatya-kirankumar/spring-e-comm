package com.ecommerce.project.service;

import com.ecommerce.project.DTO.CategoryResponse;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.CategoryModel;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryResponse getCategories() {
        List<CategoryModel> categories = categoryRepository.findAll();
        if (categories.isEmpty())
            throw new APIException("No Categories Found.");

        return new CategoryResponse(categories);
    }

    @Override
    public String addCategory(CategoryModel category) {
        String categoryName = category.getCategoryName();
        CategoryModel existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (existingCategory != null) {
            throw new APIException("Category with  name'" + categoryName + "' already exists. Please try with a different name");
        }
        categoryRepository.save(category);
        return "Category with name: " + categoryName + " added successfully.";
    }

    @Override
    public String deleteCategory(Long categoryId) {
        CategoryModel category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("categoryId", categoryId));

        categoryRepository.delete(category);
        return "Category with categoryId: " + categoryId + " removed successfully";
    }

    @Override
    public CategoryModel updateCategory(CategoryModel category, Long categoryId) {
        CategoryModel existingCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("CategoryName: " + category.getCategoryName(), "categoryId", categoryId.toString()));

        category.setCategoryId(existingCategory.getCategoryId());
        categoryRepository.save(category);

        return existingCategory;
    }
}