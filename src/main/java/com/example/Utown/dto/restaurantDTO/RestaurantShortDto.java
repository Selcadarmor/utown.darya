package com.example.Utown.dto.restaurantDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantShortDto {
    private Long id;
    private String title;
    private String phone;
}