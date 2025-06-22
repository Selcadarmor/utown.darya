package com.example.Utown.dto.restaurantAdminDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantAdminUpdateDto {
    @NotBlank
    private String username;

    @NotBlank
    private Long defaultAddress;

    @NotBlank
    private String transport;

    @NotBlank
    private Long addressId;


}
