package com.example.Utown.dto.orderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewOrderNotificationDto {
    private Long orderId;
    private String orderNumber;
    private BigDecimal deliveryPrice;
    private String fullAddress;
    private BigDecimal totalSum;
    private LocalDateTime createdAt;
    private String clientPhone;
}
