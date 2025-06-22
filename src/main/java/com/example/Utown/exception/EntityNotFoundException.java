package com.example.Utown.exception;


public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException(Long id) {
    super("Restaurant not found with id: " + id);
  }
}
