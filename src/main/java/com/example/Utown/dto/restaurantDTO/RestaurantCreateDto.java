package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.addressDTO.AddressInfoDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateDto;
import com.example.Utown.dto.fileInfoDTO.FileInfoDetailsDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.restaurantAdminDTO.RestaurantAdminCreateDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.model.RestaurantCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantCreateDto {
    private String title;
    private String description;
    private String phone;
    private BigDecimal minOrderAmount;
    private FileInfoDetailsDto fileInfo;
    private List<RestaurantCategoryDto> categories;
    private List<OperatingModeCreateDto> operatingModes;
    private RestaurantAdminCreateDto restaurantAdmin;
    private AddressInfoDto address;
    private RestaurantAdminCreateDto restaurantCreateDto;
    private List<DishCategoryCreateDto> dishCategories;
}
