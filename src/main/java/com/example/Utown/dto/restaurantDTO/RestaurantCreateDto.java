package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.addressDTO.AddressInfoDto;
import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.dto.fileInfoDTO.FileInfoDetailsDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.restaurantAdminDTO.RestaurantAdminCreateDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
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
    private Long fileId;
    private List<RestaurantCategoryDto> categories;
    private List<OperatingModeCreateDto> operatingModes;
    private RestaurantAdminCreateDto restaurantAdmin;
    private AddressDto address;
    private RestaurantAdminCreateDto restaurantCreateDto;
    private List<DeliveryDto> deliveries;
}
