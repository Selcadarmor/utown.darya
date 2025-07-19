package com.example.Utown.dto.deliveryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryInfoDto {
    private Long id;
    private String area;
    private String district;
    private Long restaurantId;
}
