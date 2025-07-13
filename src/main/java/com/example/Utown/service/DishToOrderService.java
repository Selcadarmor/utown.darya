package com.example.Utown.service;

import com.example.Utown.dto.dishToOrderDTO.DishToOrderRequestDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderResponseDto;
import com.example.Utown.model.DishToOrder;

import java.util.List;

public interface DishToOrderService {
    DishToOrder create(Long cartId, DishToOrderRequestDto dto);
    DishToOrderResponseDto getById(Long id);
    List<DishToOrderResponseDto> getAll();
    DishToOrderResponseDto update(Long id, DishToOrderRequestDto dto);
    void delete(Long id);
    void addToCart(Long cartId, DishToOrderRequestDto dto);
    List<DishToOrderResponseDto> getAllByCartId(Long cartId);
    void clearCart(Long cartId);
}

