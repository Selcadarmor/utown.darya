package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.addressDTO.AddressInfoDto;
import com.example.Utown.dto.deliveryDTO.DeliveryInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantUpdateDto {

    private String title;
    private String description;
    private String phone;
    private BigDecimal minOrderAmount;
    private AddressInfoDto address;
    private Long fileInfoId;
    private List<OperatingModeUpdateDto> operatingModes;
    private List<Long> categoryIds;
    private List<DeliveryInfoDto> deliveries;

}
