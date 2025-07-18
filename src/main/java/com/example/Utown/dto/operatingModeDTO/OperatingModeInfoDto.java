package com.example.Utown.dto.operatingModeDTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
public class OperatingModeInfoDto {
    private LocalTime start;
    private LocalTime end;
    private boolean dayOff;
    private Integer dayOfWeek;
    private Long restaurantId;

    public OperatingModeInfoDto(LocalTime start, LocalTime end, boolean dayOff, Integer dayOfWeek, Long restaurantId) {
        this.start = start;
        this.end = end;
        this.dayOff = dayOff;
        this.dayOfWeek = dayOfWeek;
        this.restaurantId = restaurantId;
    }

}
