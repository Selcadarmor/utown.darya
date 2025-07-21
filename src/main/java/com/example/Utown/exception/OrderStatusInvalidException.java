package com.example.Utown.exception;

public class OrderStatusInvalidException extends RuntimeException {
  public OrderStatusInvalidException(String message) {
    super(message);
  }
}
