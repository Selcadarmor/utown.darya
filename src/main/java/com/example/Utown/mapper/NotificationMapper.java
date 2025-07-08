package com.example.Utown.mapper;

import com.example.Utown.dto.notificationDTO.NotificationDto;
import com.example.Utown.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDto toDto(Notification entity);

    @Mapping(target = "user", ignore = true)
    Notification toEntity(NotificationDto dto);
}

