package com.example.Utown.exception;

import lombok.Getter;

@Getter

public class ResourceNotFoundException extends RuntimeException {
  private final String entityName;
  private final Object identifier;

  public ResourceNotFoundException(String entityName, Object identifier) {
    super(String.format("%s with identifier '%s' not found", entityName, identifier));
    this.entityName = entityName;
    this.identifier = identifier;
  }
}
