package com.example.Utown.dto.clientDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
    private Long id;
    private String filePath;
    private String title;
    private List<String> categories;
    private BigDecimal deliveryPrice;
    private String deliveryTime;
}

