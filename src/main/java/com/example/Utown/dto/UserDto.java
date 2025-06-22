package com.example.Utown.dto;

import com.example.Utown.model.Address;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private Long defaultAddress;
    private String transport;
    private Long addressId;
    private boolean platform;
    private Set<Role> roles;
    private Set<Address> addresses;
    private Set<Restaurant> favoriteRestaurants;
}
