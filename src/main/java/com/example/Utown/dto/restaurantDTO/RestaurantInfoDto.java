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
@AllArgsConstructor
public class RestaurantInfoDto {
    private Long id;

    private String phone;

    private String title;

    private Address address;

    private FileInfoDetailsDto fileInfo;

    private List<OperatingModeInfoDto> operatingModes;

    private Long orderCount;

    private List<RestaurantCategoryDto> categories;

    private RestaurantAdmin restaurantAdmin;

    private Delivery delivery;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;



    public RestaurantInfoDto(Long id, String title, Long ordersCount) {
        this.id = id;
        this.title = title;
        this.orderCount = ordersCount;
    }

}
