package com.example.Utown.dto.restaurantDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
    private String imageUrl;              // Фото ресторана
    private String title;                 // Название ресторана
    private List<String> categories;     // Названия категории ресторана
    private BigDecimal deliveryPrice;    // Стоимость доставки
    private String deliveryTime;          // Время доставки
}

