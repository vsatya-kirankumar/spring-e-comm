package com.ecommerce.project.service;

import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.dto.CategoryResponse;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.CategoryModel;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<CategoryModel> categories = categoryRepository.findAll();
        if (categories.isEmpty())
            throw new APIException("No Categories Found.");

        List<CategoryDTO> categoryDTO = categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategories(categoryDTO);

        return categoryResponse;
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