package com.ecommerce.project.service;

import com.ecommerce.project.dto.CartDTO;
import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.model.UserInfo;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(ProductRepository productRepository, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        // 1. Find existing cart or create one
        Cart cart = createCart();

        // 2. Find the product and validate basic availability
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        // 3. Check if the product already exists in this cart
        List<CartItem> cartItems = cart.getCartItems();
        Optional<CartItem> existingItemOpt = cartItems.stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();

        int totalTargetQuantity = quantity;
        if (existingItemOpt.isPresent()) {
            totalTargetQuantity += existingItemOpt.get().getQuantity();
        }

        // 4. Validate stock against the total target quantity
        if (product.getQuantity() < totalTargetQuantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem cartItem;

        if (existingItemOpt.isPresent()) {
            // UPDATE: If item exists, update its quantity
            cartItem = existingItemOpt.get();
            cartItem.setQuantity(totalTargetQuantity);
        } else {
            // CREATE: If item doesn't exist, create a new one
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
            cartItem.setDiscount(product.getDiscount());
            cartItem.setProductPrice(product.getSpecialPrice());

            // Add to the local list so the mapping step at the end sees it
            cartItems.add(cartItem);
        }

        // 5. Save the CartItem (Spring Data JPA manages insert vs update via the ID)
        cartItemRepository.save(cartItem);

        // 6. Update total price of the cart and save
        // Formula: previous total + (price of new items added)
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        // 7. Map to DTO
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<ProductDTO> productDTOs = cartItems.stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity()); // Represents quantity inside the cart
            return productDTO;
        }).toList();

        cartDTO.setProducts(productDTOs);

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty()) {
            throw new APIException("No Carts found.");
        }

        List<CartDTO> cartDTOS = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> productDTOS = cart.getCartItems().stream().map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).toList();
            cartDTO.setProducts(productDTOS);

            return cartDTO;
        }).toList();

        return cartDTOS;
    }

    @Override
    public CartDTO getCartById(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if(cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(item -> item.getProduct().setQuantity(item.getQuantity()));
        List<ProductDTO> productDTOS =
                cart.getCartItems().stream().map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).toList();
        cartDTO.setProducts(productDTOS);
        return cartDTO;
    }

    @Override
    public CartDTO updateCartItemQuantity(Long productId, Integer newQuantity) {
        UserInfo user = authUtil.loggedInUser();
        Cart cart = user.getCart();
        if(cart == null) {
            throw new APIException("Cart not found");
        }

        Product product = productRepository.findById(productId).orElseThrow(() -> new APIException("Product not " +
                "found."));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "productId", productId);
        }

        int currentQuantity = cartItem.getQuantity();
        int quantityDifference = newQuantity - currentQuantity;

        // Remove product from the cart
        if(newQuantity == 0) {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);

            cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * currentQuantity));
        }else {
            // If increasing, validate stock availability
            if(quantityDifference > 0 && product.getQuantity() < newQuantity) {
                throw new APIException("Only " + product.getQuantity() + " units are available");
            }
            // Update the item quantity
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);

            // Adjust total price based on the difference (works perfectly for both negative and positive values)
            double priceAdjustment = cartItem.getProductPrice() * quantityDifference;
            cart.setTotalPrice(cart.getTotalPrice() + priceAdjustment);
        }

        // 4. Save Cart changes and return mapped DTO
        cartRepository.save(cart);

        // Convert to DTO (reuse your ModelMapper mapping logic here)
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> productDTOs = cart.getCartItems().stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        }).toList();
        cartDTO.setProducts(productDTOs);

        return cartDTO;
    }

    @Override
    public CartDTO deleteProductById(Long productId) {
        UserInfo user = authUtil.loggedInUser();
        Cart cart = user.getCart();
        if(cart == null) {
            throw new APIException("Cart not found");
        }

        productRepository.findById(productId).orElseThrow(() -> new APIException("Product not " +
                "found."));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "productId", productId);
        }

        double itemCost = cartItem.getProductPrice() * cartItem.getQuantity(); // This needs to be deducted from cart
        // total price

        // delete cartitem from the cart
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        cart.setTotalPrice(cart.getTotalPrice() - itemCost);
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<ProductDTO> productDTOs = cart.getCartItems().stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        }).toList();

        cartDTO.setProducts(productDTOs);

        return cartDTO;
    }

    @Override
    public void updateProductInCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId",
                cartId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
                "Product", "productId",
                productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "productId", productId);
        }

        // calculate cart total price
        double cartTotalPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice()); // update price of product in cart
        cart.setTotalPrice(cartTotalPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItem = cartItemRepository.save(cartItem);
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) return userCart;

        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart =  cartRepository.save(cart);

        return newCart;
    }
}