package com.example.Utown.dto.operatingModeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatingModeCreateDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean dayOff;
    private Integer dayOfWeek;
    private Long restaurantId;
}
