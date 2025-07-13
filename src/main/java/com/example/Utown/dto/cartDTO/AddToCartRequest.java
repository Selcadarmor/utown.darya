package com.example.Utown.dto.cartDTO;

import lombok.Data;

import java.util.List;

@Data
public class AddToCartRequest {
    private Long dishId;
    private int count;
    private List<Long> selectedElementIds;
}
