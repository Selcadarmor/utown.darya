package com.example.Utown.service;

import com.example.Utown.dto.notificationDTO.NotificationDto;
import com.example.Utown.model.Notification;
import com.example.Utown.model.UserType.User;

import java.util.List;

public interface NotificationService {
    void notifyUser(User user, String title, String text, boolean isSuccessful);
}

