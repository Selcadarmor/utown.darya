package com.example.Utown.dto.restaurantDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
public class RestaurantForClientDto {
    private Long id;
    private String title;
    private String filePath;
    private Set<String> categoryNames;
    private Set<Long> categoryIds;
    private BigDecimal deliveryPrice;
    private String deliveryTime;
    private Boolean isRecommended;
    private Boolean isActive;

}



