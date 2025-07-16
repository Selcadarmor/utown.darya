package com.example.Utown.controller;
import com.example.Utown.dto.ApiErrorResponse;
import com.example.Utown.exception.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            UserNotFoundException.class,
            AddressNotFoundException.class,
            DishNotFoundException.class,
            NotificationNotFoundException.class,
            OrderNotFoundException.class,
            RestaurantNotFoundException.class,
            RoleNotFoundException.class,
            ResourceNotFoundException.class,
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @Operation(hidden = true)
    public ApiErrorResponse handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), path);
    }


    @ExceptionHandler({
            InvalidJwtTokenException.class,
            ExpireJwtTokenException.class,
            RefreshTokenNotFoundException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @Operation(hidden = true)
    public ApiErrorResponse handleJwtException(RuntimeException ex, HttpServletRequest request) { /// Jwt ошибки
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @Operation(hidden = true)
    public ApiErrorResponse handleConflictException(RuntimeException ex, HttpServletRequest request) { ///ошибка 409 например дублирование и др...
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Operation(hidden = true)
    public ApiErrorResponse handleAllOthers(Exception ex, HttpServletRequest request) {/// ошибка сервера 500
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request.getRequestURI());
    }

    @ExceptionHandler({
            CartIsEmptyException.class,
            RatingOutOfRangeException.class,
            InvalidArgumentException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Operation(hidden = true)
    public ApiErrorResponse handleBadRequest(RuntimeException ex, HttpServletRequest request) {/// ошибка 400
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    private ApiErrorResponse buildError(HttpStatus status, String message, String path) { /// дополнительный метод что бы не прописывать каждый раз в обработке
        return ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .build();
    }

    @ExceptionHandler(DishNotInCartException.class)
    public ResponseEntity<?> handleDishNotInCart(DishNotInCartException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND.value())
                        .error("Not Found")
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Operation(hidden = true)
    public ApiErrorResponse handleMissingRequestParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        String message = String.format("Missing required parameter: '%s'", ex.getParameterName());
        return buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<?> handleCartNotFound(CartNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "message", ex.getMessage(),
                "status", 400,
                "error", "Bad Request"
        ));
    }
}
