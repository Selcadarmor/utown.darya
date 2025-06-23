package com.example.Utown.dto;

import com.example.Utown.model.enumFiles.OrderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Getter
public class OrderShortDto {
    private Long id;
    private String number;
    private String date;
    private BigDecimal totalSum;
    private OrderStatus status;
}
