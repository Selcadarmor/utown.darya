package com.example.Utown.dto.dishDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishDto {
    private Long id;
    private String description;
    private Boolean isActive;
    private Boolean isDeleted;
    private BigDecimal price;
    private Integer sort;
    private String title;
    private Long fileId;
    private Long dishCategoryId;
    private Long restaurantId;
}


