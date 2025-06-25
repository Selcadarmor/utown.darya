package com.example.Utown.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Api Error")
public class ApiErrorResponse {
    @Schema(description = "Date and time error")
    private LocalDateTime timestamp;
    @Schema(description = "Error message")
    private String message;
    @Schema(description = "Error status code")
    private int status;
    @Schema(description = "Reason for the error")
    private String error;
    @Schema(description = "Error path")
    private String path;
}