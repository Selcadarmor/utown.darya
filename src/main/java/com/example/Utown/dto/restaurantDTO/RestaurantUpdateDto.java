package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.addressDTO.AddressInfoDto;
import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;
import com.example.Utown.model.RestaurantCategory;
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
    private FileInfoDto fileInfo;
    private List<OperatingModeUpdateDto> operatingModes;
    private List<RestaurantCategory> categories;
    //private RestaurantAdminCreateDto restaurantAdmin;
}
