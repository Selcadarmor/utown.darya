package com.example.Utown.dto.dishToOrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishToOrderRequestDto {
    private Long dishId;
    private Integer count;
    private List<Long> selectedElementIds;
}

