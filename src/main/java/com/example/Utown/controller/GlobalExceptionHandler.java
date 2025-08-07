package com.example.Utown.controller;

import com.example.Utown.dto.ApiErrorResponse;
import com.example.Utown.exception.AccessDeniedToOrderException;
import com.example.Utown.exception.CartIsEmptyException;
import com.example.Utown.exception.DefaultAddressNotSetException;
import com.example.Utown.exception.DifferentRestaurantException;
import com.example.Utown.exception.ExpireJwtTokenException;
import com.example.Utown.exception.InvalidArgumentException;
import com.example.Utown.exception.InvalidJwtTokenException;
import com.example.Utown.exception.OrderCancelNotAllowedException;
import com.example.Utown.exception.InvalidOperationException;
import com.example.Utown.exception.RatingOutOfRangeException;
import com.example.Utown.exception.RefreshTokenNotFoundException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.exception.RestaurantAlreadyFavoritedException;
import com.example.Utown.exception.RestaurantNotInFavoritesException;
import com.example.Utown.exception.RoleNotFoundException;
import com.example.Utown.exception.S3UploadException;
import com.example.Utown.exception.UserAlreadyExistsException;
import com.example.Utown.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UserNotFoundException.class,
            RoleNotFoundException.class,
            ResourceNotFoundException.class
    })
    @Operation(hidden = true)
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        log.warn("Not Found: {} - {}", request.getRequestURI(), ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler({
            InvalidJwtTokenException.class,
            ExpireJwtTokenException.class,
            RefreshTokenNotFoundException.class
    })
    @Operation(hidden = true)
    public ResponseEntity<ApiErrorResponse> handleJwtException(RuntimeException ex, HttpServletRequest request) {
        log.warn("JWT Exception: {} - {}", request.getRequestURI(), ex.getMessage());
        return buildError(HttpStatus.UNAUTHORIZED, ex, request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @Operation(hidden = true)
    public ResponseEntity<ApiErrorResponse> handleConflictException(RuntimeException ex, HttpServletRequest request) {
        log.warn("Conflict: {} - {}", request.getRequestURI(), ex.getMessage());
        return buildError(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler({
            CartIsEmptyException.class,
            RatingOutOfRangeException.class,
            InvalidArgumentException.class,
            InvalidOperationException.class,
            OrderCancelNotAllowedException.class,
            DefaultAddressNotSetException.class,
            RestaurantAlreadyFavoritedException.class,
            RestaurantNotInFavoritesException.class,
            DifferentRestaurantException.class
    })
    @Operation(hidden = true)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        log.warn("Bad request: {} - {}", request.getRequestURI(), ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(AccessDeniedToOrderException.class)
    @Operation(hidden = true)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedToOrder(AccessDeniedToOrderException ex, HttpServletRequest request) {
        log.warn("Access denied to order: {} - {}", request.getRequestURI(), ex.getMessage());
        return buildError(HttpStatus.FORBIDDEN, ex, request);
    }

    @ExceptionHandler(S3UploadException.class)
    @Operation(hidden = true)
    public ResponseEntity<ApiErrorResponse> handleS3UploadException(S3UploadException ex, HttpServletRequest request) {
        log.error("S3 Upload failed: {} - {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    @ExceptionHandler(Exception.class)
    @Operation(hidden = true)
    public ResponseEntity<ApiErrorResponse> handleAllOthers(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error at [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, new RuntimeException("Internal server error"), request);
    }

    private ResponseEntity<ApiErrorResponse> buildError(HttpStatus status, Throwable ex, HttpServletRequest request) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, status);
    }
}