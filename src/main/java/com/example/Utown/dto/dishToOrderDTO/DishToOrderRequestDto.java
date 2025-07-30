package com.example.Utown.dto.dishToOrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class DishToOrderRequestDto {
    private Integer count;
    private List<Long> selectedElementIds;
}

