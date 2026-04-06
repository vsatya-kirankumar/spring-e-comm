package com.ecommerce.project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {
    private String message;
    private List<CategoryDTO> categories;

    public CategoryResponse(String message) {
        this.message = message;
    }

    public CategoryResponse(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    public CategoryResponse() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }
}