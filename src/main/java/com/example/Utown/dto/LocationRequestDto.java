package com.example.Utown.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequestDto {
    private  Long entityId;
    private String entityType;
    private String address;
}
