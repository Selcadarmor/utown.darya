package com.example.Utown.controller;
import com.example.Utown.dto.ApiErrorResponse;
import com.example.Utown.exception.*;
import com.example.Utown.exception.InvalidArgumentException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

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
    @Operation(hidden =true) /// скрыввем для Swagger
    public ApiErrorResponse handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
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
}
