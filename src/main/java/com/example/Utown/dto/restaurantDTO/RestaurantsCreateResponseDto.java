package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.restaurantAdminDTO.RestaurantAdminCreateDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryCreateDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantsCreateResponseDto {
    private String title;
    private String description;
    private String phone;
    private BigDecimal minOrderAmount;
    private FileInfoDto file;
    private List<OperatingModeCreateDto> operatingModes;
    private AddressDto address;
    private RestaurantAdminCreateDto restaurantAdmin;
    private List<DeliveryDto> deliveries;
    private Set<RestaurantCategoryDto> categories;
}
