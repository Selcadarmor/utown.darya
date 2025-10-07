package com.example.Utown.dto.restaurantAdminDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantAdminInfoDto {
    private Long id;
    private String fullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}