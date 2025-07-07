package com.example.Utown.service;

import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.model.Cart;

import java.util.List;

public interface CartService {
    Cart createCart(CartDto dto);
    CartDto getCartById(Long id);
    List<CartDto> getAllCarts();
    Cart updateCart(Long id, CartDto dto);
    void deleteCart(Long id);

}


