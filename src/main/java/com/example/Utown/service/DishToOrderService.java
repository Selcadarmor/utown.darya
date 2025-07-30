package com.example.Utown.service;

import com.example.Utown.dto.dishToOrderDTO.DishToOrderRequestDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderResponseDto;
import com.example.Utown.model.DishToOrder;

import java.util.List;

public interface DishToOrderService {
    DishToOrder getById(Long id);
    List<DishToOrder> getAll();
    DishToOrder create(Long cartId, Long dishId, DishToOrderRequestDto dto);
    DishToOrderResponseDto update(Long id, DishToOrderRequestDto dto);
    void delete(Long id);

    List<DishToOrderResponseDto> getAllByCartId(Long cartId);
}

