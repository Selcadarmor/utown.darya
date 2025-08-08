package com.example.Utown.dto.dishToOrderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishInCartDto {
    private Long dishId;
    private String title;
    private String description;
    private String filePath;
    private BigDecimal price;
    private Integer count;
    private BigDecimal sum;
    private Set<String> elementNames;
}


