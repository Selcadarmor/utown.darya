package com.example.Utown.dto.operatingModeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatingModeUpdateDto {
    private Long id;
    private Integer dayOfWeek;
    private LocalTime start;
    private LocalTime end;
    private boolean dayOff;
    private Long restaurantId;
}
