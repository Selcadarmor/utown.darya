package com.example.Utown.service;

import com.example.Utown.dto.orderDTO.OrderDto;
import com.example.Utown.model.Order;
import com.example.Utown.dto.orderDTO.OrderShortDto;

import java.util.List;

public interface OrderService {
    Order create(OrderDto dto);
    OrderDto getById(Long id);
    List<OrderDto> getAll();
    Order update(Long id, OrderDto dto);
    void delete(Long id);
}

