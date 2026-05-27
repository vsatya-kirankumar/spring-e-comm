package com.ecommerce.project.controller;

import com.ecommerce.project.dto.CartDTO;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

    @Autowired
    private AuthUtil authUtil;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);

        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> cartDTOList = cartService.getAllCarts();

        return new ResponseEntity<>(cartDTOList, HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getUserCart() {
        String emailId = authUtil.loggedInEmail();
        Long cartId = authUtil.loggedInUser().getCart().getCartId();

        CartDTO cartDTO = cartService.getCartById(emailId, cartId);

        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/cart/products/{productId}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable Long productId, @RequestBody Map<String, Integer> payload) {
        Integer quantity = payload.get("quantity");
        if (quantity == null || quantity < 1) {
            throw new APIException("Invalid quantity provided");
        }

        CartDTO updatedCart = cartService.updateCartItemQuantity(productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/admin/products/{productId}")
    public String deleteProduct(@PathVariable Long productId) {
        CartDTO cartDTO = cartService.deleteProductById(productId);

        return "Product has been removed successfully";
    }
}