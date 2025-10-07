package com.example.Utown.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(Long id) {
        super("User with this Id " + id + " already exists");
    }

    public UserAlreadyExistsException(String username) {
        super("User with username '" + username + "' already exists");
    }
}

