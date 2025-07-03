package com.example.Utown.dto.restaurantDTO;

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

    private FileInfo fileInfo;

    private List<OperatingMode> operatingModes;

    private List<Order> orders;

    private RestaurantCategory category;

    private RestaurantAdmin restaurantAdmin;

    private Delivery delivery;

    private List<Rating> grades;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
