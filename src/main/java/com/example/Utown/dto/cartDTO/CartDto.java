
package com.example.Utown.dto.cartDTO;

import com.example.Utown.dto.dishToOrderDTO.DishInCartDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class CartDto {
    private List<DishInCartDto> dishes;
    private BigDecimal deliveryPrice;
    private BigDecimal totalSum;
}
