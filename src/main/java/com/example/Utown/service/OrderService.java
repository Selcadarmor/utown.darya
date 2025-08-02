package com.example.Utown.service;

import com.example.Utown.dto.orderDTO.DailyOrderStatsDto;
import com.example.Utown.dto.orderDTO.MonthlyOrderStatsDto;
import com.example.Utown.dto.orderDTO.OrderDetailsDto;
import com.example.Utown.dto.orderDTO.OrderDto;
import com.example.Utown.model.Order;
import com.example.Utown.model.UserType.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.List;

public interface OrderService {
    Order getById(Long id);
    List<Order> getAll();
    void delete(Long id);
    Order createOrderFromCart();
    void acceptOrder(Long orderId);
    void rejectOrder(Long orderId);
    void cancelOrderByClient(Long orderId, Long clientId);
    void cancelOrderByAdmin(Long orderId);
    List<OrderDto> getAllOrdersForRestaurantAdmin(String username);
    List<OrderDto> getOrdersByStatusForRestaurantAdmin(String username, String statusStr);
    Page<OrderDetailsDto> getOrderDetailsByClient(Long clientId, String query, Pageable pageable);
    List<MonthlyOrderStatsDto> getMonthlyStats(int year);
    List<DailyOrderStatsDto> getDailyOrders(YearMonth month);
}

