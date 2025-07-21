package com.example.Utown.service;

import com.example.Utown.dto.orderDTO.OrderDetailsDto;
import com.example.Utown.dto.orderDTO.OrderDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.OrderMapper;
import com.example.Utown.model.Cart;
import com.example.Utown.model.Dish;
import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.Order;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.model.enumFiles.OrderStatus;
import com.example.Utown.repository.CartRepository;
import com.example.Utown.repository.DishToOrderRepository;
import com.example.Utown.repository.OrderRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import com.example.Utown.repository.UserType.RestaurantAdminRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final ClientRepository clientRepository;
    private final OrderMapper orderMapper;
    private final DishToOrderRepository dishToOrderRepository;
    private final CartRepository cartRepository;
    private final RestaurantAdminRepository restaurantAdminRepository;
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

    @Transactional
    public Order createOrderFromCart(Client client) {
        Cart cart = client.getCart();
        if (cart == null || cart.getDishToOrders().isEmpty()) {
            throw new IllegalStateException("Корзина пуста");
        }

        List<DishToOrder> cartItems = cart.getDishToOrders();
        Dish sampleDish = cartItems.stream()
                .map(DishToOrder::getDish)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Корзина пуста"));

        Restaurant restaurant = sampleDish.getRestaurant();

        Order order = Order.builder()
                .client(client)
                .restaurant(restaurant)
                .status(OrderStatus.PENDING)
                .totalSum(cart.getTotalSum())
                .deliveryPrice(cart.getDeliveryPrice())
                .orderPrice(cart.getSumOrder())
                .createdAt(LocalDateTime.now())
                .isPaid(false)
                .build();

        List<DishToOrder> copiedItems = cartItems.stream().map(item -> {
            DishToOrder copy = new DishToOrder();
            copy.setDish(item.getDish());
            copy.setCount(item.getCount());
            copy.setSum(item.getSum());
            copy.setSelectedElements(item.getSelectedElements());
            copy.setOrder(order);
            copy.setCart(null);
            return copy;
        }).toList();

        order.setDishesToOrder(copiedItems);
        orderRepository.save(order); // каскадно сохранит и блюда

        // Очистка корзины
        dishToOrderRepository.deleteAll(cartItems);
        cart.getDishToOrders().clear();
        cart.setTotalDish(0);
        cart.setTotalSum(BigDecimal.ZERO);
        cart.setSumOrder(BigDecimal.ZERO);
        cartRepository.save(cart);

        return order;
    }

    public void cancelOrderByClient(Long orderId, Long clientId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getClient().getId().equals(clientId)) {
            throw new AccessDeniedException("You can cancel only your orders");
        }

        if (order.getStatus() == OrderStatus.COMPLETED ||
                order.getStatus() == OrderStatus.REJECTED ||
                order.getStatus() == OrderStatus.CANCELED ||
                order.getStatus() == OrderStatus.DELIVERY) {
            throw new IllegalStateException("Order cannot be canceled at this stage");
        }

        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    public void cancelOrderByAdmin(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        // Защита от повторной отмены
        if (order.getStatus() == OrderStatus.COMPLETED ||
                order.getStatus() == OrderStatus.CANCELED) {
            throw new IllegalStateException("Order cannot be canceled at this stage");
        }

        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    public List<OrderDto> getAllOrdersForRestaurantAdmin(String username) {
        RestaurantAdmin admin = restaurantAdminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
        List<Order> orders = orderRepository.findByRestaurant(admin.getRestaurant());
        return orders.stream().map(orderMapper::orderToDto).toList();
    }

    public List<OrderDto> getOrdersByStatusForRestaurantAdmin(String username, String statusStr) {
        RestaurantAdmin admin = restaurantAdminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        OrderStatus status;
        try {
            status = OrderStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + statusStr);
        }

        List<Order> orders = orderRepository.findByRestaurantAndStatus(admin.getRestaurant(), status);
        return orders.stream().map(orderMapper::orderToDto).toList();
    }


    @Override
    @Transactional
    public void acceptOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be accepted");
        }

        order.setStatus(OrderStatus.PROCESSING);
        order.setTimeOfAccepted(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void rejectOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be rejected");
        }

        order.setStatus(OrderStatus.REJECTED);
        orderRepository.save(order);
    }

    @Override
    public Page<OrderDetailsDto> getOrderDetailsByClient(Long clientId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByClientId(clientId, pageable);

        List<OrderDetailsDto> dtos = orders.getContent().stream()
                .map(order -> {
                    List<String> dishTitles = dishToOrderRepository.findDishTitleByOrderId(order.getId());

                    return new OrderDetailsDto(
                            order.getClientPhone(),
                            order.getClient().getFullName(),
                            order.getFullAddress(),
                            order.getRestaurant().getTitle(),
                            order.getRestaurant().getAddress().getFullAddress(),
                            order.getRestaurantPhone(),
                            order.getNumber(),
                            order.getTotalSum(),
                            order.getTimeOfAccepted(),
                            order.getTimeOfDelivery(),
                            order.getTimeOfSending(),
                            dishTitles
                    );
                })
                .toList();
        return  new PageImpl<>(dtos, pageable, orders.getTotalElements());

    }
}
