package com.example.Utown.service.order;

import com.example.Utown.dto.orderDto.OrderShortDto;
import com.example.Utown.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderServiceImpl  implements OrderService{
    private final OrderRepository orderRepository;

    @Override
    public List<OrderShortDto> getClientOrders(Long clientId) {
        return orderRepository.findAllOrdersByClientId(clientId);
    }
}
