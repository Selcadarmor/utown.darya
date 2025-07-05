package com.example.Utown.dto.clientDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
public class RestaurantForClientDto {
    private Long id;
    private String filePath;
    private String title;
    private Set<String> categoryNames;
    private Set<Long> categoryIds;
    private BigDecimal deliveryPrice;
    private String deliveryTime;
    private Boolean isActive;

    public RestaurantForClientDto(Long id, String filePath, String title, Set<String> categoryNames, BigDecimal deliveryPrice, String deliveryTime, Boolean isActive) {
        this.id = id;
        this.filePath = filePath;
        this.title = title;
        this.categoryNames = categoryNames;
        this.deliveryPrice = deliveryPrice;
        this.deliveryTime = deliveryTime;
        this.isActive = isActive;
    }
}



