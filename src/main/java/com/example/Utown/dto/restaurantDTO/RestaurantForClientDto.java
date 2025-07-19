package com.example.Utown.dto.restaurantDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RestaurantForClientDto {
    private Long id;
    private String title;
    private String filePath;
    private String description;
    private BigDecimal deliveryPrice;
    private String deliveryTime;
    private Boolean isRecommended;
    private Boolean isActive;
    private Boolean isDeleted;

    public RestaurantForClientDto(Long id, String title, String filePath, String description,
                                  BigDecimal deliveryPrice, String deliveryTime,
                                  Boolean isRecommended, Boolean isActive, Boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.filePath = filePath;
        this.description = description;
        this.deliveryPrice = deliveryPrice;
        this.deliveryTime = deliveryTime;
        this.isRecommended = isRecommended;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
    }

}



