package com.example.Utown.dto.cartDTO;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long dishId;
    private Long elementId;
    private int count;
}
