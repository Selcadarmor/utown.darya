package com.example.Utown.service;

import com.example.Utown.dto.orderDTO.OrderDto;
import com.example.Utown.model.Order;
import com.example.Utown.model.UserType.Client;

import java.util.List;

public interface OrderService {
    Order create(OrderDto dto);
    OrderDto getById(Long id);
    List<OrderDto> getAll();
    Order update(Long id, OrderDto dto);
    void delete(Long id);
    Order createOrderFromCart(Client client);
    void acceptOrder(Long orderId);
    void rejectOrder(Long orderId);
    void cancelOrderByClient(Long orderId, Long clientId);
    void cancelOrderByAdmin(Long orderId);
    List<OrderDto> getAllOrdersForRestaurantAdmin(String username);
    List<OrderDto> getOrdersByStatusForRestaurantAdmin(String username, String statusStr);
}

