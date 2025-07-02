package com.example.Utown.service;

import com.example.Utown.dto.cartDTO.CartDto;
import java.util.List;

public interface CartService {
    CartDto createCart(CartDto dto);
    CartDto getCartById(Long id);
    List<CartDto> getAllCarts();
    CartDto updateCart(Long id, CartDto dto);
    void deleteCart(Long id);
}

