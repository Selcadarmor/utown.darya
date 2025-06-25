package com.example.Utown.dto.clientDto;

import java.math.BigDecimal;
import java.util.List;

public class RestaurantUpdateDto {
    private String imageUrl;              // Фото ресторана
    private String title;                 // Название ресторана
    private List<String> categories;     // Названия категории ресторана
    private BigDecimal deliveryPrice;    // Стоимость доставки
    private String deliveryTime;
}
