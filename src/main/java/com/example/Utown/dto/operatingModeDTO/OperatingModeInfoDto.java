package com.example.Utown.dto.operatingModeDTO;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class OperatingModeInfoDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean dayOff;
    private Integer dayOfWeek;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long restaurantId;

    public OperatingModeInfoDto(Long id, LocalDateTime start, LocalDateTime end, boolean dayOff, Integer dayOfWeek, LocalDateTime createdAt, LocalDateTime updatedAt, Long restaurantId) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.dayOff = dayOff;
        this.dayOfWeek = dayOfWeek;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.restaurantId = restaurantId;
    }

}
