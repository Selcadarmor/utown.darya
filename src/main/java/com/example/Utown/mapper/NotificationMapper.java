package com.example.Utown.mapper;

import com.example.Utown.dto.notificationDTO.NotificationDto;
import com.example.Utown.model.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDto toDto(Notification entity);
    Notification toEntity(NotificationDto dto);
}