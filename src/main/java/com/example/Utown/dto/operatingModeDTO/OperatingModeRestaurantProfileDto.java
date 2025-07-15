package com.example.Utown.dto.operatingModeDTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OperatingModeRestaurantProfileDto {
        private Integer dayOfWeek;
        private LocalDateTime start;
        private LocalDateTime end;
        private Boolean dayOff;

        public OperatingModeRestaurantProfileDto(Integer dayOfWeek, LocalDateTime start, LocalDateTime end, Boolean dayOff) {
                this.dayOfWeek = dayOfWeek;
                this.start = start;
                this.end = end;
                this.dayOff = dayOff;
        }
}
