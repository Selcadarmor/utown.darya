package com.example.Utown.dto.clientDto;

import lombok.Data;

@Data
public class DishCategoriesDto { //Список категорий для блюд
    private Long id;
    private String name; // название категории
    private String imageUrl;
    private int restaurantCount;
}



