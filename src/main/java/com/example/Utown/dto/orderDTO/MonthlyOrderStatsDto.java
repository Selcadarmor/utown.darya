package com.example.Utown.dto.orderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyOrderStatsDto {
    private String month;              // например, "July 2025"
    private BigDecimal totalAmount;   // общая сумма заказов
    private Integer totalOrders;          // всего заказов
    private Integer cancelledOrders;
}
