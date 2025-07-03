package com.example.Utown.dto.clientDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RestaurantForClientDto {
    private Long id;
    private String filePath;
    private String title;
    private String categoryName;
    private BigDecimal deliveryPrice;
    private String deliveryTime;

    public RestaurantForClientDto(Long id, String filePath, String title, String categoryName, BigDecimal deliveryPrice, String deliveryTime) {
        this.id = id;
        this.filePath = filePath;
        this.title = title;
        this.categoryName = categoryName;
        this.deliveryPrice = deliveryPrice;
        this.deliveryTime = deliveryTime;
    }

}

