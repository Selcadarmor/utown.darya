package com.example.Utown.dto.orderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyOrderStatsDto {
    private LocalDate date;
    private BigDecimal totalAmount;
    private int totalOrders;
    private int cancelledOrders;
    private List<OrderShortInfoDto> orders;
}
