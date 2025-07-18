package com.example.Utown.dto.dishToOrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishToOrderResponseDto {
    private Long id;
    private String dishName;
    private BigDecimal dishPrice;
    private Integer count;
    private BigDecimal sum;
    private List<String> selectedElementNames;
}
