package com.example.Utown.dto.dishToOrderDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DishInCartDto {
    private Long dishId;
    private String title;
    private String description;
    private String filePath;
    private BigDecimal price;
    private Integer count;
    private BigDecimal sum;
}

