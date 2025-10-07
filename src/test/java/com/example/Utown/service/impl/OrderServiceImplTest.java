package com.example.Utown.service.impl;

import com.example.Utown.model.Order;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.enumFiles.OrderStatus;
import com.example.Utown.repository.OrderRepository;
import com.example.Utown.service.NotificationService;
import com.example.Utown.service.OrderServiceImpl;
import com.example.Utown.service.UserTypeService.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testCancelOrderByClient_sendsNotification() {
        // given
        Long orderId = 1L;
        Long clientId = 10L;

        Client client = new Client();
        client.setId(clientId);
        client.setUsername("test_client");

        Order order = new Order();
        order.setId(orderId);
        order.setNumber("ORD-123");
        order.setClient(client);
        order.setStatus(OrderStatus.PENDING);

        when(clientService.getCurrentClient()).thenReturn(client);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // when
        orderService.cancelOrderByClient(orderId);

        // then
        assertEquals(OrderStatus.CANCELED, order.getStatus());

        verify(orderRepository).save(order);

        verify(notificationService).notifyUser(
                eq(client),
                eq("Order cancelled"),
                eq("You have successfully cancelled order №" + order.getNumber()),
                eq(true)
        );
    }
}
