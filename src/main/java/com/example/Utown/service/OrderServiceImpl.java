package com.example.Utown.service;

import com.example.Utown.dto.orderDTO.OrderDetailsDto;
import com.example.Utown.dto.orderDTO.OrderDto;
import com.example.Utown.exception.CartIsEmptyException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.OrderMapper;
import com.example.Utown.model.Address;
import com.example.Utown.model.Cart;
import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.Order;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.model.enumFiles.DeliveryStatus;
import com.example.Utown.model.enumFiles.OrderStatus;
import com.example.Utown.repository.DishToOrderRepository;
import com.example.Utown.repository.OrderRepository;
import com.example.Utown.repository.UserType.RestaurantAdminRepository;
import com.example.Utown.service.UserTypeService.ClientService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final CartService cartService;
    private final DishToOrderRepository dishToOrderRepository;
    private final OrderMapper orderMapper;
    private final RestaurantAdminRepository restaurantAdminRepository;
    private final AddressService addressService;

    // ========================= GET =========================

    @Override
    public Order getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
        return order;
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Page<OrderDetailsDto> getOrderDetailsByClient(Long clientId, String query, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllWithFilter(query, clientId, pageable);

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

    // ========================= POST =========================

    @Override
    @Transactional
    public Order createOrderFromCart() {
        Client client = clientService.getCurrentClient();
        Cart cart = cartService.getCartById(client.getCart().getId());
        Address clientAddress = addressService.getAddressById(client.getDefaultAddress());

        if (cart.getDishToOrders().isEmpty()) {
            throw new CartIsEmptyException();
        }

        List<DishToOrder> cartItems = cart.getDishToOrders();
        Restaurant restaurant = cartItems.get(0).getDish().getRestaurant();

        Order order = Order.builder()
                .area(clientAddress.getArea())
                .city(clientAddress.getCity())
                .clientPhone(client.getUsername())
                .date(LocalDate.now())
                .deliveryPrice(cart.getDeliveryPrice())
                .details(clientAddress.getDetails())
                .fullAddress(clientAddress.getFullAddress())
                .isPaid(true) //Позже доработать оплату
                .payment(null)
                .latitude(clientAddress.getLatitude())
                .longitude(clientAddress.getLongitude())
                .noteForCourier(null) //какие еще note???
                .number(generateOrderNumber())
                .orderPrice(cart.getSumOrder())
                .postcode(clientAddress.getPostCode())
                .restaurantPhone(restaurant.getPhone())
                .state(clientAddress.getState())
                .status(OrderStatus.PENDING)
                .street(clientAddress.getStreet())
                .timeOfAccepted(null)
                .timeOfDelivery(restaurant.getDeliveryTime())
                .timeOfSending(null)
                .totalSum(cart.getTotalSum())
                .typeAddress(clientAddress.getTypeAddress())
                .cookingTime(null)
                .deliveryStatus(DeliveryStatus.WAITING_FOR_ORDER_ACCEPTED)
                .endTimeOfCooking(null)
                .intercomCode(clientAddress.getIntercomCode())
                .restaurant(restaurant)
                .client(client)
                .build();

        List<DishToOrder> dishToOrders = moveDishToOrdersFromCartToOrder(cart, order); //неправильно ошибка связей и элементов
        order.setDishesToOrder(dishToOrders);

        orderRepository.save(order);

        cartService.clearCartWithoutDeletion(cart);

        return order;
    }

    // ========================= PUT ==========================


    // ========================= DELETE =========================

    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", id));
        orderRepository.delete(order);
    }

    @Override
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
        order.setTimeOfAccepted(LocalTime.now());
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

    // ========================= PRIVATE =========================

    private String generateOrderNumber() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        long countToday = orderRepository.countByDate(startOfDay, endOfDay);
        return String.format("%03d", countToday + 1);
    }

    private List<DishToOrder> moveDishToOrdersFromCartToOrder(Cart cart, Order order) {
        return cart.getDishToOrders()
                .stream()
                .peek(dishToOrder -> {
                    dishToOrder.setCart(null);
                    dishToOrder.setOrder(order);
                })
                .collect(Collectors.toList());
    }


}
