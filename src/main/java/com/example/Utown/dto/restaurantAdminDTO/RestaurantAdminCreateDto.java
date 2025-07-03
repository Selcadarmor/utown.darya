package com.example.Utown.dto.restaurantAdminDTO;

import com.example.Utown.model.Notification;
import com.example.Utown.model.enumFiles.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantAdminCreateDto {
    private String username;
    private String password;
    private String fullName;
    private Set<Roles> roles;
    private Set<Notification> notifications;
    private Boolean isActive;
}
