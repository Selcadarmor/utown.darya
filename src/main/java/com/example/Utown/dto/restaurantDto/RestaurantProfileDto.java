package com.example.Utown.dto.restaurantDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantProfileDto {// профиль ресторана

    private String imageUrl;
    private BigDecimal rating;
    private String deliveryTime;
    private String title;
    private String description;
    private BigDecimal minOrderPrice;

//    private List<OperatingModeDto> operatingModes; // График работы
}

