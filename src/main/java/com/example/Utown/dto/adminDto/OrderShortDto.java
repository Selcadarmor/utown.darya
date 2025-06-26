package com.example.Utown.dto.adminDto;

import com.example.Utown.model.enumFiles.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderShortDto {
    private Long id;
    private BigDecimal totalSum;
    private LocalDateTime createdAt;
    private OrderStatus status;

    public OrderShortDto(Long id, BigDecimal totalSum, LocalDateTime createdAt, OrderStatus status) {
        this.id = id;
        this.totalSum = totalSum;
        this.createdAt = createdAt;
        this.status = status;
    }
}
