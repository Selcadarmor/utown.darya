package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.dto.notificationDTO.NotificationDto;
import com.example.Utown.dto.restaurantAdminDTO.RestaurantAdminCreateDto;
import com.example.Utown.model.*;
import com.example.Utown.model.UserType.RestaurantAdmin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantCreateDto {
    private String title;
    private String description;
    private String phone;
    private BigDecimal minOrderAmount;
    private List<Order> orders;
    private FileInfo fileInfo;
    private RestaurantCategory category;
    private List<OperatingMode> operatingModes;
    private RestaurantAdmin restaurantAdmin;
    private Boolean isRecommended;
    private BigDecimal rating;
    private String status;
    private Integer totalRatings;
    private Boolean statusForcedChanged;
    private Boolean isActive;
    private Delivery delivery;
    private String deliveryTime;
    private String facilities;
    private Address address;
    private List<Rating> grades;
    private Set<NotificationDto> notifications;
    private RestaurantAdminCreateDto restaurantCreateDto;

}
