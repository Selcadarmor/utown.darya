package com.example.Utown.exception;

public class InvalidJwtTokenException extends RuntimeException {
    public InvalidJwtTokenException() {
        super("Invalid Jwt Token");
    }
}
