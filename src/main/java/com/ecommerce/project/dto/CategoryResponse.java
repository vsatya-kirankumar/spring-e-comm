package com.ecommerce.project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {
    private String message;
    private List<CategoryDTO> categories;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean isLastPage;

    public CategoryResponse(String message) {
        this.message = message;
    }

    public CategoryResponse(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    public CategoryResponse() {
    }
}