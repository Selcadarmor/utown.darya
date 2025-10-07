package com.example.Utown.dto.deliveryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryInfoDto {
    private Long id;
    private String area;
    private String district;
    private BigDecimal price;
    private Long restaurantId;
}
