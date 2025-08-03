package com.example.Utown.service;

import com.example.Utown.dto.dishToOrderDTO.DishInCartDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderRequestDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderResponseDto;
import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.Order;

import java.util.List;
import java.util.Set;

public interface DishToOrderService {
    DishToOrder getById(Long id);
    List<DishInCartDto> getDishesInCart(Long cartId);
    Set<String> getElementNames(Long dishToOrderId);
    List<DishToOrder> getAll();
    DishToOrder createByCart(Long cartId, Long dishId, DishToOrderRequestDto dto);
    List<DishToOrder> createByOrder(List<DishToOrder> cartDishToOrders, Order order);
    void update(Long dishToOrderId, DishToOrderRequestDto dto);
    void delete(Long id);
    void deleteAll(List<DishToOrder> dishes);
}

