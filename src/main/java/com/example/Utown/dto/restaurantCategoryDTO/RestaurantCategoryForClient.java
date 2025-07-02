package com.example.Utown.dto.restaurantCategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantCategoryForClient {
    private Long id;
    private String name;
    private String imageUrl;
    private Long restaurantCount;

    public RestaurantCategoryForClient(String name) {
        this.name = name;
    }
}




