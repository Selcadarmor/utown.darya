package com.example.Utown.exception;

import java.math.BigDecimal;

public class RatingOutOfRangeException extends RuntimeException {
    public RatingOutOfRangeException(BigDecimal rating) {
        super("Rating must be between 0 and 5, but was " + rating);
    }
}
