package com.example.Utown.dto.dishToOrderDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@AllArgsConstructor
public class DishInOrderHistoryDto {
    private String title;
    private Integer count;
    private BigDecimal sum;
    private Set<String> elementName;
}
