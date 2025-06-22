package com.example.Utown.dto;

import com.example.Utown.model.Address;
import com.example.Utown.model.FileInfo;
import com.example.Utown.model.OperatingMode;
import com.example.Utown.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDto {
    private Long id;
    private String deliveryTime;
    private String description;
    private Boolean isRecommended;
    private BigDecimal minOrderAmount;
    private String phone;
    private BigDecimal rating;
    private String title;
    private Integer totalRatings;
    private Boolean statusForcedChanged;
    private Boolean isActive;
    private Address address;
    private User user;
    private FileInfo fileInfo;
    private List<OperatingMode> operatingModes;
    private Long addressId;
    private Long RestaurantAdminId;
}
