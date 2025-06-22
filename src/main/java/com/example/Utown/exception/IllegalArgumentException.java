package com.example.Utown.exception;

public class IllegalArgumentException extends RuntimeException {
    public IllegalArgumentException(Long id) {
        super("Client not found with id: " + id);
    }
}
