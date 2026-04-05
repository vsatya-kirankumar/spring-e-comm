package com.ecommerce.project.DTO;

import com.ecommerce.project.model.CategoryModel;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {
    private String message;
    private List<CategoryModel> categories;

    public CategoryResponse(String message) {
        this.message = message;
    }

    public CategoryResponse(List<CategoryModel> categories) {
        this.categories = categories;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }
}
