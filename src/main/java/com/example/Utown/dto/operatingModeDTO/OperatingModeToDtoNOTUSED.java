package com.example.Utown.dto.operatingModeDTO;

import com.example.Utown.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatingModeToDtoNOTUSED {

        private Long id;
        private Integer dayOfWeek;
        private LocalDateTime start;
        private LocalDateTime end;
        private Boolean dayOff;
        private Restaurant restaurant;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

}
