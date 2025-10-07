package com.example.Utown.dto.operatingModeDTO;

import lombok.Data;

import java.time.LocalTime;


@Data
public class OperatingModeInfoDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean dayOff;
    private Integer dayOfWeek;
    private Long restaurantId;

    public OperatingModeInfoDto(LocalTime startTime, LocalTime endTime, boolean dayOff, Integer dayOfWeek, Long restaurantId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOff = dayOff;
        this.dayOfWeek = dayOfWeek;
        this.restaurantId = restaurantId;
    }

}
