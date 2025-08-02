package com.example.Utown.service;

import com.example.Utown.dto.orderDTO.DailyOrderStatsDto;
import com.example.Utown.dto.orderDTO.MonthlyOrderStatsDto;
import com.example.Utown.dto.orderDTO.OrderDetailsDto;
import com.example.Utown.dto.orderDTO.OrderDto;
import com.example.Utown.dto.orderDTO.OrderHistoryDto;
import com.example.Utown.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.List;

public interface OrderService {
    Order getById(Long id);
    List<Order> getAll();
    Page<OrderHistoryDto> getOrderHistoryByClient( Pageable pageable);
    List<OrderDto> getAllOrdersForRestaurantAdmin(String username);
    List<OrderDto> getOrdersByStatusForRestaurantAdmin(String username, String statusStr);
    Page<OrderDetailsDto> getOrderDetailsByClient(Long clientId, String query, Pageable pageable);
    Order createOrderFromCart();
    void cancelOrderByClient(Long orderId);
    void acceptOrder(Long orderId, Integer cookingTime);
    void cancelOrderByAdmin(Long orderId);
    void readyOrder(Long orderId);
    void completedOrder(Long orderId);
    void delete(Long id);
    List<MonthlyOrderStatsDto> getMonthlyStats(int year);
    List<DailyOrderStatsDto> getDailyOrders(YearMonth month);
}

