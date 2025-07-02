package com.example.Utown.dto.operatingModeDto;

import lombok.*;

import java.time.LocalDateTime;


@Data
public class OperatingModeDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean dayOff;
    private Integer dayOfWeek;

    public OperatingModeDto(Long id, LocalDateTime start, LocalDateTime end, boolean dayOff, Integer dayOfWeek) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.dayOff = dayOff;
        this.dayOfWeek = dayOfWeek;
    }
}
