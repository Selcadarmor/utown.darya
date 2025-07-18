package com.example.Utown.dto.dishToOrderDTO;

import com.example.Utown.model.Cart;
import com.example.Utown.model.Dish;
import com.example.Utown.model.Element;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishToOrderRequestDto {
    private Long dishId;
    private Integer count;
    private List<Long> selectedElementIds;
}

