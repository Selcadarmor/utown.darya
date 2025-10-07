package com.example.Utown.dto.notificationDTO;

import com.example.Utown.model.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Long id;                  // если нужен
    private String title;             // "Новый заказ", "Обновлён статус"
    private String text;              // подробности
    private String date;              // например, "2025-08-05"
    private String time;              // например, "15:45"
    private Boolean isSuccessful;     // для стиля (цвет иконки и т.п.)

    public static NotificationDto from(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .text(notification.getText())
                .date(notification.getDate())
                .time(notification.getTime())
                .isSuccessful(notification.getIsSuccessful())
                .build();
    }
}

