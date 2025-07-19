package com.example.Utown.dto.dishCategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishCategoryRestaurantProfileDto {
    private Long id;
    private String name;
    private Integer sort;
    private Boolean isActive;
    private String filePath;
    private Long dishCount;
}
