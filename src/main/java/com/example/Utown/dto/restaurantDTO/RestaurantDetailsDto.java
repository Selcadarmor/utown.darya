package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.restaurantAdminDTO.RestaurantAdminInfoDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.model.OperatingMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class RestaurantDetailsDto {
    private Long id;//убрать
    private String title;
    private String description;
    private String phone;
    private BigDecimal minOrderAmount;
    private Long orderCount;
    private Long fileId;
    private List<RestaurantCategoryDto> categories;//выбрать нужное поле
    private List<OperatingModeInfoDto> operatingModes;
    private RestaurantAdminInfoDto restaurantAdmin;//убпвть
    private List<DeliveryDto> deliveries;//нужные поля

    public RestaurantDetailsDto(Long id, String title, String description, String phone,
                                BigDecimal minOrderAmount, Long orderCount, Long fileId,
                                List<RestaurantCategoryDto> categories,
                                List<OperatingModeInfoDto> operatingModes,
                                RestaurantAdminInfoDto restaurantAdmin,
                                List<DeliveryDto> deliveries) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.phone = phone;
        this.minOrderAmount = minOrderAmount;
        this.orderCount = orderCount;
        this.fileId = fileId;
        this.categories = categories;
        this.operatingModes = operatingModes;
        this.restaurantAdmin = restaurantAdmin;
        this.deliveries = deliveries;
    }
    public RestaurantDetailsDto(Long id, String title, String description, String phone,
                                BigDecimal minOrderAmount, Long orderCount, Long fileId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.phone = phone;
        this.minOrderAmount = minOrderAmount;
        this.orderCount = orderCount;
        this.fileId = fileId;
    }
}
