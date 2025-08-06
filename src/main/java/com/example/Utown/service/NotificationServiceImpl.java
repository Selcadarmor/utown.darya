package com.example.Utown.service;

import com.example.Utown.dto.notificationDTO.NotificationDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.NotificationMapper;
import com.example.Utown.model.Notification;
import com.example.Utown.model.UserType.User;
import com.example.Utown.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void notifyUser(User user, String title, String text, boolean isSuccessful) {
        log.info("Notifying user {}: {} - {}", user.getUsername(), title, text);

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .text(text)
                .isSuccessful(isSuccessful)
                .date(LocalDate.now().toString())
                .time(LocalTime.now().toString())
                .build();

        repository.save(notification);
        log.info("Notification saved successfully: {}", notification.getId());

        // Можно отправить DTO, чтобы избежать отправки полной сущности
        NotificationDto dto = NotificationDto.from(notification);
        log.debug("Sending notification to user {}: {}", user.getUsername(), dto);
        messagingTemplate.convertAndSend("/topic/notifications/" + user.getId(), dto);
        log.info("Notification sent successfully to user {}", user.getUsername());
    }

}

