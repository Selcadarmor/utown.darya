package com.example.Utown.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishToOrderDto { //блюдо на заказ
    private String imageUrl;
    private String dishName;
    private String description;
//    private List<OptionSelectionDto> selectedOptions;
}

