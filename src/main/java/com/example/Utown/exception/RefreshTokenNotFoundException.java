package com.example.Utown.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException() {
        super("Refresh Token not found or invalid");
    }
}
