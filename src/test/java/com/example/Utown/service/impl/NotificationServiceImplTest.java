package com.example.Utown.service.impl;

import com.example.Utown.dto.notificationDTO.NotificationDto;
import com.example.Utown.model.Order;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.NotificationRepository;
import com.example.Utown.service.NotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void testNotifyUserOnOrderCancel() {
        Client client = new Client();
        client.setId(1L);
        client.setUsername("testuser");

        Order order = new Order();
        order.setId(100L);
        order.setNumber("A123");
        order.setClient(client);

        notificationService.notifyUser(
                client,
                "Order Cancelled",
                "Your order №" + order.getNumber() + " has been cancelled by the restaurant.",
                false
        );

        verify(messagingTemplate).convertAndSend(
                eq("/topic/notifications/" + client.getId()),
                any(NotificationDto.class)
        );

        verify(notificationRepository).save(any()); // проверка, что сохранение произошло
    }
}