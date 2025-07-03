package com.example.Utown.dto.restaurantCategoryDTO;

import com.example.Utown.model.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantCategoryForClient {
    private Long id;
    private String name;
    private String filePath;
    private Long restaurantCount;

}




