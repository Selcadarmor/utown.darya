package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantUpdateDto {

    private String title;
    private String description;
    private String phone;
    private BigDecimal minOrderAmount;
    private AddressDto address;
    private Long fileId;
    private List<OperatingModeUpdateDto> operatingModes;
    private Set<Long> categoryIds;
    private List<DeliveryDto> deliveries;

}
