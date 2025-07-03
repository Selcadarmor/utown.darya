package com.example.Utown.dto.adminDto;

import com.example.Utown.dto.operatingModeDTO.OperatingModeDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RestaurantCreateUpdateDto {
    private Long id;
    private String title;
    private String description;
    private String phone;
    private BigDecimal minOrderAmount;
    private String city;
    private String name;
    private Long fileInfoId;
    private List<OperatingModeDto> operatingModes;


    public RestaurantCreateUpdateDto(Long id, String title, String description, String phone, BigDecimal minOrderAmount, String city, String name, Long fileInfoId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.phone = phone;
        this.minOrderAmount = minOrderAmount;
        this.city = city;
        this.name= name;
        this.fileInfoId = fileInfoId;
    }
}
