package com.example.Utown.service;

import com.example.Utown.dto.orderDTO.OrderDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.OrderMapper;
import com.example.Utown.model.Order;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.OrderRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final ClientRepository clientRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order create(OrderDto dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurant().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", dto.getRestaurant().getId()));
        Client client = clientRepository.findById(dto.getClient().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found", dto.getClient().getId()));

        Order order = orderMapper.orderDtoToEntity(dto);
        order.setRestaurant(restaurant);
        order.setClient(client);

        return orderRepository.save(order);
    }

    @Override
    public OrderDto getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", id));
        return orderMapper.orderToDto(order);
    }

    @Override
    public List<OrderDto> getAll() {
        return orderRepository.findAll().stream()
                .map(orderMapper::orderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Order update(Long id, OrderDto dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", id));

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurant().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", dto.getRestaurant().getId()));
        Client client = clientRepository.findById(dto.getClient().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found", dto.getClient().getId()));

        // Маппим вручную только обновляемые поля
        Order updated = orderMapper.orderDtoToEntity(dto);
        updated.setId(id);
        updated.setRestaurant(restaurant);
        updated.setClient(client);

        return orderRepository.save(updated);
    }

    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", id));
        orderRepository.delete(order);
    }
}

