package com.example.Utown.exception;

public class NoCartForClientException extends RuntimeException {
    public NoCartForClientException(String message) {
        super(message);
    }
}
