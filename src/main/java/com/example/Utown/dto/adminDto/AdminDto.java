package com.example.Utown.dto.adminDto;

import com.example.Utown.dto.otherDto.AddressDto;
import com.example.Utown.dto.otherDto.RestaurantDto;
import com.example.Utown.dto.otherDto.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
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
}
