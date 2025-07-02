package com.example.Utown.dto.restaurantDto;

import com.example.Utown.dto.operatingModeDto.OperatingModeDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RestaurantDetailsDto {
    private Long id;
    private String title;
    private String description;
    private String phone;
    private String city;
    private String area;
    private BigDecimal minOrderAmount;
    private Long orderCount;
    private Long fileInfoId;
    private String  name;
    private List<OperatingModeDto> operatingModes;

    public RestaurantDetailsDto(Long id, String title, String description, String phone, String city, String area, BigDecimal minOrderAmount, Long orderCount, Long fileInfoId, String name) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.phone = phone;
        this.city = city;
        this.area = area;
        this.minOrderAmount = minOrderAmount;
        this.orderCount = orderCount;
        this.fileInfoId = fileInfoId;
        this.name = name;
    }
}
