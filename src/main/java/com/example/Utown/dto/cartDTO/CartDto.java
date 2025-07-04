package com.example.Utown.dto.cartDTO;

import com.example.Utown.model.Dish;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class CartDto {
    private Long id;
    private BigDecimal deliveryPrice;
    private BigDecimal sumOrder;
    private Integer totalDish;
    private BigDecimal totalSum;
    private List<Dish> dish;

    public CartDto(Long id, BigDecimal deliveryPrice, BigDecimal sumOrder, Integer totalDish, BigDecimal totalSum, List<Dish> dish) {
        this.id = id;
        this.deliveryPrice = deliveryPrice;
        this.sumOrder = sumOrder;
        this.totalDish = totalDish;
        this.totalSum = totalSum;
        this.dish = dish;
    }

}


