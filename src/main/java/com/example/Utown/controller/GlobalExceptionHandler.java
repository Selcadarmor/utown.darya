package com.example.Utown.controller;
import com.example.Utown.dto.ApiError;
import com.example.Utown.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @Operation(hidden =true) /// скрыввем для Swagger
    public ApiError handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) { /// метод если пользователь не найден
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
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
