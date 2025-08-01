package com.example.Utown.service;

import com.example.Utown.dto.dishToOrderDTO.DishInCartDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderRequestDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderResponseDto;
import com.example.Utown.model.DishToOrder;

import java.util.List;

public interface DishToOrderService {
    DishToOrder getById(Long id);
    List<DishInCartDto> getDishesInCart(Long cartId);
    List<DishToOrder> getAll();
    DishToOrder createByCart(Long cartId, Long dishId, DishToOrderRequestDto dto);
    void update(Long dishToOrderId, DishToOrderRequestDto dto);
    void delete(Long id);
    void deleteAll(List<DishToOrder> dishes);
}

