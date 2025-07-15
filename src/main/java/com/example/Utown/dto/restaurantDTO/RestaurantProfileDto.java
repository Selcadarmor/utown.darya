package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.operatingModeDTO.OperatingModeRestaurantProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantProfileDto {
    private Long id;
    private String title;
    private String phone;
    private String filePath;
    private String description;
    private String deliveryTime;
    private Double totalRating;
    private BigDecimal minOrderAmount;
    private List<OperatingModeRestaurantProfileDto> operatingModes;
}

