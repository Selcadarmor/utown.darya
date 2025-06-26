package com.example.Utown.dto.adminDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RestaurantCreateUpdateDto {
    private String title;
    private  String description;
    private String phone;
    private BigDecimal minOrderAmount;
    private String city;
    private String categoryTitle;
    private Long fileInfoId;

    public RestaurantCreateUpdateDto(String title, String description, String phone, BigDecimal minOrderAmount, String city, String categoryTitle, Long fileInfoId) {
        this.title = title;
        this.description = description;
        this.phone = phone;
        this.minOrderAmount = minOrderAmount;
        this.city = city;
        this.categoryTitle = categoryTitle;
        this.fileInfoId = fileInfoId;
    }
}
