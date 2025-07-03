package com.example.Utown.dto.operatingModeDTO;

import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.Client;
import lombok.*;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatingModeInfoDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean dayOff;
    private Integer dayOfWeek;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Restaurant restaurant;

}
