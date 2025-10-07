package com.example.Utown.dto.orderDTO;

import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDto {
    private Long id;
    private String area;
    private RestaurantDetailsDto restaurantDto;
}