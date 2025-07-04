package com.example.Utown.service;

import com.example.Utown.dto.notificationDTO.NotificationDto;
import com.example.Utown.model.Notification;

import java.util.List;

public interface NotificationService {
    Notification create(NotificationDto dto);
    NotificationDto getById(Long id);
    List<NotificationDto> getAll();
    Notification update(Long id, NotificationDto dto);
    void delete(Long id);
}

