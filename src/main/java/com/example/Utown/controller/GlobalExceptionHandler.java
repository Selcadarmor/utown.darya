package com.example.Utown.controller;
import com.example.Utown.dto.ApiError;
import com.example.Utown.exception.*;
import com.example.Utown.exception.InvalidArgumentException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
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
    public ApiError handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) { /// метод если пользователь не найден 400
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }


    @ExceptionHandler({
            InvalidJwtTokenException.class,
            ExpireJwtTokenException.class,
            RefreshTokenNotFoundException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @Operation(hidden = true)
    public ApiError handleJwtException(RuntimeException ex, HttpServletRequest request) { /// Jwt ошибки
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @Operation(hidden = true)
    public ApiError handleConflictException(RuntimeException ex, HttpServletRequest request) { ///ошибка 409 например ублирование и др...
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Operation(hidden = true)
    public ApiError handleAllOthers(Exception ex, HttpServletRequest request) {/// ошибка сервера 500
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request.getRequestURI());
    }

    @ExceptionHandler({
            CartIsEmptyException.class,
            RatingOutOfRangeException.class,
            InvalidArgumentException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Operation(hidden = true)
    public ApiError handleBadRequest(RuntimeException ex, HttpServletRequest request) {/// ошибка 404
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    private ApiError buildError(HttpStatus status, String message, String path) { /// дополнительный метод что бы не прописывать каждый раз в обработке
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .build();
    }
}
