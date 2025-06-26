package com.example.Utown.dto.adminDto;

import lombok.*;

import java.time.LocalDateTime;


@Data
public class OperatingModeDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean dayOff;

    public OperatingModeDto(Long id, LocalDateTime start, LocalDateTime end, boolean dayOff) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.dayOff = dayOff;
    }
}
