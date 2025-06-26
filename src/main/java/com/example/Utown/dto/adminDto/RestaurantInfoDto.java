package com.example.Utown.dto.adminDto;

import lombok.Data;

@Data
public class RestaurantInfoDto {
    private Long id;
    private String title;
    private String phone;
    private Long orderCount;
    private String city;
    private String category;

    public RestaurantInfoDto(Long id, String title, String phone, String city, String category, Long orderCount) {
        this.id = id;
        this.title = title;
        this.phone = phone;
        this.city = city;
        this.category = category;
        this.orderCount = orderCount;
    }

}
