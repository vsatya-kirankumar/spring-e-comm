package com.ecommerce.project.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private List<ProductDTO> content;
    private Integer pageSize;
    private Long totalElements;
    private Integer pageNumber;
    private Integer totalPages;
    private boolean lastPage;
}