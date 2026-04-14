package com.ecommerce.project.controller;

import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    /*
        Add Product: /admin/categories/{categoryId}/product
        get all products: /public/products
        get product by category: /public/categories/{categoryId}/products
        get products by keyword: /public/products/keyword/{keyword}
        update product: /product/{productId}
     */

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody Product product, @PathVariable Long categoryId) {

        ProductDTO productResponse = productService.addProduct(categoryId, product);

        return new ResponseEntity<ProductDTO>(productResponse, HttpStatus.CREATED);
    }
}