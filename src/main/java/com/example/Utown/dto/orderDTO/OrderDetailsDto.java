package com.example.Utown.dto.orderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDto {
    private String clientPhone;
    private String clientName;
    private String fullAddress;
    private String restaurantTitle;
    private String restaurantAddress;
    private String restaurantPhone;
    private String number;
    private BigDecimal totalSum;
    private String timeOfAccepted;
    private String timeOfDelivery;
    private String timeOfSending;
    private List<String> dishTitle;
}
