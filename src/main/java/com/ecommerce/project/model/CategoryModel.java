package com.ecommerce.project.model;

import com.ecommerce.project.exception.NotBlankGroup;
import com.ecommerce.project.exception.SizeGroup;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @NotBlank(message = "Category Name Must Not be empty. Please enter a valid name.",groups = NotBlankGroup.class)
    @Size(min = 5, message = "Category Name Must contain at least 5 characters.",groups = SizeGroup.class)
    private String categoryName;
}
