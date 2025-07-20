package com.example.Utown.dto.operatingModeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatingModeCreateDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean dayOff;
    private Integer dayOfWeek;
    private Long restaurantId;
}
