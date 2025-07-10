package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.model.OperatingMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDetailsDto {
    private Long id;
    private String title;
    private String description;
    private String phone;
    private BigDecimal minOrderAmount;
    private Long orderCount;
    private Long fileId;
    private List<Long> categoryIds;
    private List<Long> operatingModeIds;
    private Long restaurantAdminId;
    private List<Long> deliveryIds;
}
