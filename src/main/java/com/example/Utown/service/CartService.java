package com.example.Utown.service;

import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderRequestDto;
import com.example.Utown.model.Cart;
import com.example.Utown.model.DishToOrder;

public interface CartService {
    Cart getCartById(Long id);
    Cart createCart();
    Cart updateCart(Long id, CartDto dto);
    DishToOrder addDishToCart(Long dishId,DishToOrderRequestDto dto);
    void clearCart(Long cartId);

}



