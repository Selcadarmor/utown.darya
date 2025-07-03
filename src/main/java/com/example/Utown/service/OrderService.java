package com.example.Utown.service;

import com.example.Utown.dto.orderDto.OrderShortDto;

import java.util.List;

public interface OrderService {
    List<OrderShortDto> getClientOrders(Long clientId);
}
