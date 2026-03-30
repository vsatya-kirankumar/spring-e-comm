package com.ecommerce.project.service;

import com.ecommerce.project.model.CategoryModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private List<CategoryModel> categories = new ArrayList<CategoryModel>();

    @Override
    public List<CategoryModel> getCategories() {
        return categories;
    }

    @Override
    public String addCategory(CategoryModel category) {
        categories.add(category);
        String categoryName = category.getCategoryName();

        return "Category with name: " + categoryName + " added successfully.";
    }

    @Override
    public String deleteCategory(Long categoryId) {
        CategoryModel category = categories.stream().filter(cat -> cat.getCategoryId().equals(categoryId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found"));

        categories.remove(category);
        return "Category with categoryId: " + categoryId + " removed successfully";
    }

    @Override
    public String updateCategory(CategoryModel category, Long categoryId) {
        CategoryModel existingCategory = categories.stream().filter(cat -> cat.getCategoryId().equals(categoryId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found"));

        if (existingCategory != null) {
            int index = categories.indexOf(existingCategory);
            categories.set(index, category);
            return "Category with categoryId: " + category.getCategoryId() + " updated successfully";
        }

        return null;
    }
}