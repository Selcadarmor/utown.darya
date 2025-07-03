package com.example.Utown.dto.restaurantDTO;


import com.example.Utown.dto.addressDTO.AddressInfoDto;
import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantUpdateDto {
    private Long id;
    private String title;
    private String description;
    private String phone;
    private BigDecimal minOrderAmount;
    private AddressInfoDto address;
    private FileInfoDto fileInfo;
    private List<OperatingModeInfoDto> operatingModes;
    private RestaurantCategoryDto category;
}
