package com.example.Utown.exception;

public class ElementNotFoundException extends RuntimeException {
    public ElementNotFoundException(String message) {
        super(message);
    }

    public ElementNotFoundException(Long elementId) {
        super("Element with ID " + elementId + " not found");
    }
}
