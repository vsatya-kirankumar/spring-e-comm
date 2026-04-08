package com.ecommerce.project.service;

import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.dto.CategoryResponse;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.CategoryModel;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CategoryModel> allCategories = categoryRepository.findAll(pageable);

        List<CategoryModel> categories = allCategories.getContent();
        if (categories.isEmpty())
            throw new APIException("No Categories Found.");

        List<CategoryDTO> categoryDTO = categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategories(categoryDTO);
        categoryResponse.setTotalPages(allCategories.getTotalPages());
        categoryResponse.setTotalElements(allCategories.getTotalElements());
        categoryResponse.setPageNumber(allCategories.getNumber());
        categoryResponse.setPageSize(allCategories.getSize());

        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        CategoryModel category = modelMapper.map(categoryDTO, CategoryModel.class);
        CategoryModel existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (existingCategory != null) {
            throw new APIException("Category with name '" + category.getCategoryName() + "' already exists. Please try with a different name");
        }
        CategoryModel savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        CategoryModel category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("categoryId", categoryId));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        CategoryModel categoryModel = modelMapper.map(categoryDTO, CategoryModel.class);
        CategoryModel existingCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("CategoryName: " + categoryDTO.getCategoryName(), "categoryId", categoryId.toString()));

        categoryModel.setCategoryId(existingCategory.getCategoryId());
        categoryRepository.save(categoryModel);

        return modelMapper.map(categoryModel, CategoryDTO.class);
    }
}