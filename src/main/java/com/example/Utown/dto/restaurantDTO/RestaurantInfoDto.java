package com.example.Utown.dto.restaurantDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@Data
@NoArgsConstructor
public class RestaurantInfoDto {
    private Long restaurantId;

    private String phone;

    private String title;

    private String city;

    private Long orderCount;

    //private LocalDateTime createdAt;

    //private LocalDateTime updatedAt;



    public RestaurantInfoDto(Long restaurantId, String title, String city, String phone, Long ordersCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.restaurantId = restaurantId;
        this.title = title;
        this.city = city;
        this.phone = phone;
        this.orderCount = ordersCount;
        //this.createdAt = createdAt;
       // this.updatedAt = updatedAt;
    }

}
