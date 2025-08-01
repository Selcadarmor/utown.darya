package com.example.Utown.dto.dishCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishCategoryDto {
    private Long id;
    private String name;
    private Integer sort;
    private Boolean isActive;
    private Long restaurantId;
    private Long fileId;
}

