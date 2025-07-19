package com.example.Utown.dto.operatingModeDTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class OperatingModeRestaurantProfileDto {
        private Integer dayOfWeek;
        private LocalTime start;
        private LocalTime end;
        private Boolean dayOff;

        public OperatingModeRestaurantProfileDto(Integer dayOfWeek, LocalTime start, LocalTime end, Boolean dayOff) {
                this.dayOfWeek = dayOfWeek;
                this.start = start;
                this.end = end;
                this.dayOff = dayOff;
        }
}
