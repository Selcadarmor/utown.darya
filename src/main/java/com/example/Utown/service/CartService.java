package com.example.Utown.service;

import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.model.Cart;

import java.util.List;

public interface CartService {
    Cart getCartById(Long id);
    List<Cart> getAllCarts();
    Cart createCart(CartDto dto);
    Cart updateCart(Long id, CartDto dto);
    void deleteCart(Long id);

}



