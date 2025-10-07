package com.example.Utown.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponseDto {
    private Long entityId;
    private String entityType;
    private String fullAddress;
}