package com.example.Utown.service;

import com.example.Utown.dto.cartDTO.AddToCartRequest;
import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.model.Cart;

import java.util.List;

public interface CartService {
    Cart createCart(Long clientId);
    Cart createCart(Long clientId, CartDto dto);
    CartDto getCartById(Long id);
    List<CartDto> getAllCarts();
    Cart updateCart(Long id, CartDto dto);
    void deleteCart(Long id);
    void addDishToCart(Long clientId, AddToCartRequest request);
    void updateDishInCart(Long clientId, AddToCartRequest request);
    void clearCart(Long clientId);
    void removeDishFromCart(Long clientId, Long dishId, Long elementId);
}


