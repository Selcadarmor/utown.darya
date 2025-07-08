package com.example.Utown.dto.notificationDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private String date;
    private Integer typeNotification;
    private Integer typeOrder;
    private Integer typeRestaurant;
    private Integer typeUser;
    private String text;
    private String timeNotification;
    private String timeOrder;
    private String timeRestaurant;
    private String timeUser;
    private String time;
    private String title;
    private String errorMessage;
    private Boolean isSuccessful;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    //private Long notificationId;
}

