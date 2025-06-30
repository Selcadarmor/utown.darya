package com.example.Utown.exception;

import lombok.Getter;

@Getter
public class InvalidArgumentException extends RuntimeException {
    private final String field;
    private final Object invalidValue;
    public InvalidArgumentException(String field, Object invalidValue) {
        super(String.format("Invalid value for '%s' : %s", field, invalidValue));
        this.field = field;
        this.invalidValue = invalidValue;
    }
}
