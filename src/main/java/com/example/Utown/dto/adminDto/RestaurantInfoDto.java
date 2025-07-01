package com.example.Utown.dto.adminDto;

import lombok.Data;



@Data
public class RestaurantInfoDto {
    private Long id;
    private String title;
    private String phone;
    private Long orderCount;
    private String city;


    public RestaurantInfoDto(Long id, String title, String phone, String city, Long orderCount) {
        this.id = id;
        this.title = title;
        this.phone = phone;
        this.city = city;
        this.orderCount = orderCount;
    }

}
