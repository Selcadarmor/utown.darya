package com.example.Utown.dto.operatingModeDTO;

import lombok.Data;
import java.time.LocalTime;

@Data
public class OperatingModeRestaurantProfileDto {
        private Integer dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        private Boolean dayOff;

        public OperatingModeRestaurantProfileDto(Integer dayOfWeek, LocalTime startTime, LocalTime endTime, Boolean dayOff) {
                this.dayOfWeek = dayOfWeek;
                this.startTime = startTime;
                this.endTime = endTime;
                this.dayOff = dayOff;
        }
}
