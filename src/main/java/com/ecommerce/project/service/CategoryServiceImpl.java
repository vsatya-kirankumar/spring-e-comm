package com.ecommerce.project.service;

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
    private List<CategoryModel> categories = new ArrayList<CategoryModel>();

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryModel> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public String addCategory(CategoryModel category) {
        categoryRepository.save(category);
        String categoryName = category.getCategoryName();

        return "Category with name: " + categoryName + " added successfully.";
    }

    @Override
    public String deleteCategory(Long categoryId) {
        CategoryModel category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found"));

        categoryRepository.delete(category);
        return "Category with categoryId: " + categoryId + " removed successfully";
    }

    @Override
    public CategoryModel updateCategory(CategoryModel category, Long categoryId) {
        CategoryModel existingCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found"));

        category.setCategoryId(existingCategory.getCategoryId());
        categoryRepository.save(category);

        return existingCategory;
    }
}