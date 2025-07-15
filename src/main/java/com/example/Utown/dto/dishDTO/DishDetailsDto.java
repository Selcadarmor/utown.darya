package com.example.Utown.dto.dishDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishDetailsDto {
    private Long id;
    private String description;
    private Boolean isActive;
    private Boolean isDeleted;
    private BigDecimal price;
    private Integer sort;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long restaurantId;
    private Long dishCategoryId;
    private Long fileId;
    // убрать рстаурант айди и использовать его в контроллере
    //убрать createdAt and updatedAt and id
}
