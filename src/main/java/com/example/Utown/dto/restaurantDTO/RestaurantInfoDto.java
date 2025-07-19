package com.example.Utown.dto.restaurantDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

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
