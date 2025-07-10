package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.fileInfoDTO.FileInfoDetailsDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.model.*;
import com.example.Utown.model.UserType.RestaurantAdmin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
public class RestaurantInfoDto {
    private Long restaurantId;

    private String phone;

    private String title;

    private String city;

    private Long orderCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;



    public RestaurantInfoDto(Long restaurantId, String title, String city, String phone, Long ordersCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.restaurantId = restaurantId;
        this.title = title;
        this.city = city;
        this.phone = phone;
        this.orderCount = ordersCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
