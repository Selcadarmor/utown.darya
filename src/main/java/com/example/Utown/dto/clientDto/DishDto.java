package com.example.Utown.dto.clientDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishDto {
    private Long id;                  // ID блюда
    private String name;              // Название блюда
    private String description;       // Описание блюда
    private String imageUrl;          // Фото блюда
    private BigDecimal price;         // Цена блюда
    private BigDecimal deliveryPrice; // Стоимость доставки
    private String restaurantName;    // Название ресторана
}

