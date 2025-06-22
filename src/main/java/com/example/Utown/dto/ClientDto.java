package com.example.Utown.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private Long id;
    private String username;
    private Long defaultAddress;
    private String transport;
    private Long addressId;
    private boolean platform;
    private Boolean isActive;
    private Set<RoleDto> roles;
    private Set<AddressDto> addresses;
    private Set<RestaurantDto> favoriteRestaurants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
