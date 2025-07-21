package com.example.Utown.exception;

public class DefaultAddressNotSetException extends RuntimeException {
    public DefaultAddressNotSetException() {
        super("Client has no default address set");
    }
}

