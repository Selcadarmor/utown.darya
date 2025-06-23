package com.example.Utown.dto.otherDto;

import com.example.Utown.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    private Long id;
    private String area;
    private BigDecimal price;
    private String district;
    private Boolean isActive;
    private Boolean isDeleted;
    private Restaurant restaurant;


}
