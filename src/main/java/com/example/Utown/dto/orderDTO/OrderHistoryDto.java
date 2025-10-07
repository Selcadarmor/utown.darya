package com.example.Utown.dto.orderDTO;

import com.example.Utown.dto.dishToOrderDTO.DishInOrderHistoryDto;
import com.example.Utown.model.enumFiles.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryDto {
    private String clientPhone;
    private String clientName;
    private String fullAddress;
    private String restaurantTitle;
    private String restaurantPhone;
    private OrderStatus orderStatus;
    private String number;
    private BigDecimal totalSum;
    private List<DishInOrderHistoryDto> dishes;
}
