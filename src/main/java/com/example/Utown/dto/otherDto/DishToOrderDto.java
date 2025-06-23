package com.example.Utown.dto.otherDto;

import com.example.Utown.model.Cart;
import com.example.Utown.model.Dish;
import com.example.Utown.model.Order;
import com.example.Utown.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishToOrderDto {
    private Long id;
    private Integer count;
    private BigDecimal sum;
    private Cart cart;
    private Dish dish;
    private Order order;
    private Restaurant restaurant;
}
