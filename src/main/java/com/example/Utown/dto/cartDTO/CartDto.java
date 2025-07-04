
package com.example.Utown.dto.cartDTO;

import com.example.Utown.model.DishToOrder;
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
    private List<DishToOrder> dishToOrder;

    public CartDto(Long id, BigDecimal deliveryPrice, BigDecimal sumOrder, Integer totalDish, BigDecimal totalSum, List<DishToOrder> dishToOrder) {
        this.id = id;
        this.deliveryPrice = deliveryPrice;
        this.sumOrder = sumOrder;
        this.totalDish = totalDish;
        this.totalSum = totalSum;
        this.dishToOrder = dishToOrder;
    }
}