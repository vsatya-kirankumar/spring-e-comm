package com.ecommerce.project.service;

import com.ecommerce.project.dto.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCartById(String emailId, Long cartId);

    CartDTO updateCartItemQuantity(Long productId, Integer quantity);

    CartDTO deleteProductById(Long productId);

    void updateProductInCart(Long cartId, Long productId);
}