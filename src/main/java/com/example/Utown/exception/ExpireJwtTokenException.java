package com.example.Utown.exception;

public class ExpireJwtTokenException extends RuntimeException {
    public ExpireJwtTokenException() {
        super("Jwt Token has expired");
    }
}
