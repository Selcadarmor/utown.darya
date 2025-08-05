package com.example.Utown.service;

import com.example.Utown.dto.notificationDTO.NotificationDto;
import com.example.Utown.model.Notification;
import com.example.Utown.model.UserType.User;

import java.util.List;

public interface NotificationService {
    Notification getById(Long id);
    List<Notification> getAll();
    Notification create(NotificationDto dto);
    Notification update(Long id, NotificationDto dto);
    void delete(Long id);
    void notifyUser(User user, String title, String text, boolean isSuccessful);
}

