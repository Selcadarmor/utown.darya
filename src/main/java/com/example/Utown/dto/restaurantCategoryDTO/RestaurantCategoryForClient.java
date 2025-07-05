package com.example.Utown.dto.restaurantCategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantCategoryForClient {
    private Long id;
    private String name;
    private Integer sort;
    private Boolean isActive;
    private String filePath;
    private Long restaurantCount;

}




