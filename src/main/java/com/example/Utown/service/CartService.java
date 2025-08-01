package com.example.Utown.service;

import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderRequestDto;
import com.example.Utown.model.Cart;
import com.example.Utown.model.DishToOrder;

public interface CartService {
    Cart getCartById(Long id);
    CartDto getCart();
    Cart createCart();
    CartDto updateCart( Long dishToOrderId, DishToOrderRequestDto dto);
    DishToOrder addDishToCart(Long dishId,DishToOrderRequestDto dto);
    CartDto removeDishFromCart(Long dishToOrderId);
    CartDto clearCart(Long cartId);
    void clearCartWithoutDeletion(Cart cart);

}



