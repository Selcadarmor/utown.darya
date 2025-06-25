package com.example.Utown.dto.restaurantAdmin;

import com.example.Utown.dto.AddressDto;
import com.example.Utown.dto.RoleDto;
import com.example.Utown.dto.clientDto.RestaurantDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantAdminDto {
    private Long id;
    private String username;
    private Long defaultAddress;
    private String transport;
    private Long addressId;
    private boolean platform;
    private Set<RoleDto> roles;
    private Set<AddressDto> addresses;
    private Set<RestaurantDto> favoriteRestaurants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
}