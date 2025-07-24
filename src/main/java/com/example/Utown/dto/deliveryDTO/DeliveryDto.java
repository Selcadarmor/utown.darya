package com.example.Utown.dto.deliveryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    private Long id;
    private String area;
    private BigDecimal price;
    private String district;
    private Boolean isActive = true;
    private Boolean isDeleted = false;
    private Long restaurantId;
}


