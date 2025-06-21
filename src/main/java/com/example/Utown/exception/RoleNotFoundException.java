package com.example.Utown.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String Role) {
        super( "Could not find role " + Role);
    }
}
