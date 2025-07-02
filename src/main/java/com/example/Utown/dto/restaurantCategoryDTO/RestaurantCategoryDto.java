package com.example.Utown.dto.restaurantCategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCategoryDto {
    private Long id;
    private String imageUrl;
    private String name;
    private Integer sort;
    private Boolean isActive;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
}
