package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.deliveryDTO.DeliveryInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class RestaurantDetailsDto {
    private String title;
    private String description;
    private String phone;
    private BigDecimal minOrderAmount;
    private Long orderCount;
    private Long fileId;
    private String path;
    private Set<RestaurantCategoryDto> categories;//выбрать нужное поле
    private List<OperatingModeInfoDto> operatingModes;
    private List<DeliveryInfoDto> deliveries;

    public RestaurantDetailsDto(String title, String description, String phone,
                                BigDecimal minOrderAmount, Long orderCount, Long fileId,
                                Set<RestaurantCategoryDto> categories,
                                List<OperatingModeInfoDto> operatingModes,
                                List<DeliveryInfoDto> deliveries) {
        this.title = title;
        this.description = description;
        this.phone = phone;
        this.minOrderAmount = minOrderAmount;
        this.orderCount = orderCount;
        this.fileId = fileId;
        this.categories = categories;
        this.operatingModes = operatingModes;
        this.deliveries = deliveries;
    }
    public RestaurantDetailsDto(String title, String description, String phone,
                                BigDecimal minOrderAmount, Long orderCount, Long fileId, String path) {
        this.title = title;
        this.description = description;
        this.phone = phone;
        this.minOrderAmount = minOrderAmount;
        this.orderCount = orderCount;
        this.fileId = fileId;
        this.path = path;
    }
}
