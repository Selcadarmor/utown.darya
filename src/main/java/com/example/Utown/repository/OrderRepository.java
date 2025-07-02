package com.example.Utown.repository;

import com.example.Utown.dto.orderDto.OrderShortDto;
import com.example.Utown.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
        SELECT new com.example.Utown.dto.adminDto.OrderShortDto(
            o.id,
            o.totalSum,
            o.createdAt,
            o.status
        )
        FROM Order o
        WHERE o.client.id = :clientId
        ORDER BY o.createdAt DESC            
    """)
    List<OrderShortDto> findAllOrdersByClientId(@Param("clientId") Long clientId);
}
