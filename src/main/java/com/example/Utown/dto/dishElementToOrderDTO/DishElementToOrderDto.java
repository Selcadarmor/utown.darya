package com.example.Utown.dto.dishElementToOrderDTO;

import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.Element;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishElementToOrderDto {
    private Long id;
    private DishToOrder dishToOrder;
    private Element element;
}

