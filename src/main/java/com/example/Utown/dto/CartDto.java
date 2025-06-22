package com.example.Utown.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private BigDecimal deliveryPrice;
    private BigDecimal sumOrder;
    private Integer totalDish;
    private BigDecimal totalSum;
    private User user;
}
