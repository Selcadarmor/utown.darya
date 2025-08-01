package com.example.Utown.exception;

import com.example.Utown.model.enumFiles.OrderStatus;

public class OrderCancelNotAllowedException extends RuntimeException {
    public OrderCancelNotAllowedException(OrderStatus status) {
        super("Order cannot be canceled at status: " + status);
    }
}
