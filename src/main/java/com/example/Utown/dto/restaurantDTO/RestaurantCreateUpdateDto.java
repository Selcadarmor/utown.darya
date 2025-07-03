package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.model.Address;
import com.example.Utown.model.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantCreateUpdateDto {
    private Long id;
    private String title;
    private String description;
    private String phone;
    private BigDecimal minOrderAmount;
    private Address address;
    private String name;
    private FileInfo fileInfo;
    private List<OperatingModeInfoDto> operatingModes;


}
