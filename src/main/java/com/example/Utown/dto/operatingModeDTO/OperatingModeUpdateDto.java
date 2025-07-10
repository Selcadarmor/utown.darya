package com.example.Utown.dto.operatingModeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatingModeUpdateDto {
    private Long id;
    private Integer dayOfWeek;
    private String start;
    private String end;
    private boolean dayOff;
    private Long restaurantId;
    private LocalDateTime updatedAt;
}
