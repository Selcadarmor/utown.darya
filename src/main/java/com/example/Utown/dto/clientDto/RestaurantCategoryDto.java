package com.example.Utown.dto.clientDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantCategoryDto {
    private Long id;
    private String name;
    private String imageUrl;
    private Long restaurantCount;

    public RestaurantCategoryDto(String name) {
        this.name = name;
    }
}




