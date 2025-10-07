package com.example.Utown.dto.restaurantDTO;

import com.example.Utown.model.enumFiles.RestaurantStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantStatusUpdateRequest {
    @NotNull
    private RestaurantStatus status;
}
