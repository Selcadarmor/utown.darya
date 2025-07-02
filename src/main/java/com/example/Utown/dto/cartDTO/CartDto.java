package com.example.Utown.dto.cartDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private BigDecimal deliveryPrice;
    private BigDecimal sumOrder;
    private Integer totalDish;
    private BigDecimal totalSum;
    private Long userId;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
}
