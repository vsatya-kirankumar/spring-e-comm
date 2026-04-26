package com.ecommerce.project.model;

import com.ecommerce.project.exception.NotBlankGroup;
import com.ecommerce.project.exception.SizeGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank(message = "Product Name Must Not be empty. Please enter a valid name.",groups = NotBlankGroup.class)
    @Size(min = 5, message = "Product Name Must contain at least 5 characters.",groups = SizeGroup.class)
    private String productName;

    @NotBlank(message = "Product description Must Not be empty. Please enter a valid name.",groups = NotBlankGroup.class)
    @Size(min = 10, message = "Product Name Must contain at least 10 characters.",groups = SizeGroup.class)
    private String description;

    private String image;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

    @ManyToOne
    @JoinColumn(name = "category_Id")
    private Category category;
}