package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
public class RestaurantInfoDto {
    private Long id;

    private String phone;

    private String title;

    private String city;

    private Long orderCount;


    public RestaurantInfoDto(Long id, String title, String city, String phone, Long ordersCount) {
        this.id = id;
        this.title = title;
        this.city = city;
        this.phone = phone;
        this.orderCount = ordersCount;

    }

}
