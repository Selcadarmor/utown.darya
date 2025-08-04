package com.example.Utown.service;

import com.example.Utown.dto.dishToOrderDTO.DishInOrderHistoryDto;
import com.example.Utown.dto.orderDTO.DailyOrderStatsDto;
import com.example.Utown.dto.orderDTO.MonthlyOrderStatsDto;
import com.example.Utown.dto.orderDTO.OrderDetailsDto;
import com.example.Utown.dto.orderDTO.OrderHistoryDto;
import com.example.Utown.exception.AccessDeniedToOrderException;
import com.example.Utown.dto.orderDTO.OrderShortInfoDto;
import com.example.Utown.exception.CartIsEmptyException;
import com.example.Utown.exception.OrderCancelNotAllowedException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.exception.UserNotFoundException;
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
import com.example.Utown.service.UserTypeService.RestaurantAdminServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final CartService cartService;
    private final DishToOrderRepository dishToOrderRepository;
    private final RestaurantAdminRepository restaurantAdminRepository;
    private final AddressService addressService;
    private final DishToOrderService dishToOrderService;
    private final RestaurantAdminServiceImpl restaurantAdminService; //удалить имплементацию

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

    @Override
    @Transactional
    public Page<OrderHistoryDto> getOrderHistoryByClient(Pageable pageable) {
        Client client = clientService.getCurrentClient();

        Page<Order> orders = orderRepository.findAllByClientIdOrderByCreatedAtDesc(client.getId(), pageable);

        return orders.map(order -> {
            List<DishInOrderHistoryDto> dishes = order.getDishesToOrder().stream()
                    .map(dishToOrder -> new DishInOrderHistoryDto(
                            dishToOrder.getDish().getTitle(),
                            dishToOrder.getCount(),
                            dishToOrder.getSum(),
                            dishToOrderService.getElementNames(dishToOrder.getId())
                    ))
                    .collect(Collectors.toList());

            return new OrderHistoryDto(
                    order.getClientPhone(),
                    client.getFullName(),
                    order.getFullAddress(),
                    order.getRestaurant().getTitle(),
                    order.getRestaurantPhone(),
                    order.getStatus(),
                    order.getNumber(),
                    order.getTotalSum(),
                    dishes
            );
        });
    }

    @Override
    @Transactional
    public Page<OrderHistoryDto> getOrdersInProcessByRestaurant(Pageable pageable) {
        List<OrderStatus> activeStatuses = List.of(OrderStatus.PENDING, OrderStatus.PROCESSING);

        RestaurantAdmin restaurantAdmin = getCurrentRestaurantAdmin();
        Page<Order> orders = orderRepository.findAllByRestaurantIdAndStatusInOrderByCreatedAtDesc(
                restaurantAdmin.getRestaurant().getId(), activeStatuses, pageable
        );

        return orders.map(order -> {
            List<DishInOrderHistoryDto> dishes = order.getDishesToOrder().stream()
                    .map(dishToOrder -> new DishInOrderHistoryDto(
                            dishToOrder.getDish().getTitle(),
                            dishToOrder.getCount(),
                            dishToOrder.getSum(),
                            dishToOrderService.getElementNames(dishToOrder.getId())
                    ))
                    .collect(Collectors.toList());

            return new OrderHistoryDto(
                    order.getClientPhone(),
                    order.getClient().getFullName(),
                    order.getFullAddress(),
                    order.getRestaurant().getTitle(),
                    order.getRestaurantPhone(),
                    order.getStatus(),
                    order.getNumber(),
                    order.getTotalSum(),
                    dishes
            );
        });
    }

    @Override
    @Transactional
    public Page<OrderHistoryDto> getOrdersCompletedByRestaurant(Pageable pageable) {
        List<OrderStatus> statuses = List.of(
                OrderStatus.READY_FOR_PICKUP,
                OrderStatus.DELIVERY,
                OrderStatus.COMPLETED,
                OrderStatus.CANCELED
        );

        RestaurantAdmin restaurantAdmin = getCurrentRestaurantAdmin();

        Page<Order> orders = orderRepository.findAllByRestaurantIdAndStatusInOrderByCreatedAtDesc(
                restaurantAdmin.getRestaurant().getId(), statuses, pageable
        );

        return orders.map(order -> {
            List<DishInOrderHistoryDto> dishes = order.getDishesToOrder().stream()
                    .map(dishToOrder -> new DishInOrderHistoryDto(
                            dishToOrder.getDish().getTitle(),
                            dishToOrder.getCount(),
                            dishToOrder.getSum(),
                            dishToOrderService.getElementNames(dishToOrder.getId())
                    ))
                    .collect(Collectors.toList());

            return new OrderHistoryDto(
                    order.getClientPhone(),
                    order.getClient().getFullName(),
                    order.getFullAddress(),
                    order.getRestaurant().getTitle(),
                    order.getRestaurantPhone(),
                    order.getStatus(),
                    order.getNumber(),
                    order.getTotalSum(),
                    dishes
            );
        });
    }


    @Override
    public List<MonthlyOrderStatsDto> getMonthlyStats(int year) { //Делаем историю для одного года
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        Long restaurantId = currentAdmin.getRestaurant().getId(); // проверка на админа


        List<Order> orders = orderRepository.findAllByRestaurantIdAndDateBetween(
                restaurantId,
                LocalDate.of(year, 1, 1),//начало
                LocalDate.of(year, 12, 31)//конец
        );
        Map<Month, List<Order>> ordersByMonth = orders.stream()//создаем карту хранения ордеров и  месяц и группируем по дате конкретно по месяцу
                .collect(Collectors.groupingBy(order -> order.getDate().getMonth()));
        return ordersByMonth.entrySet().stream()//возващаем через стрим каждый элемент карты  вытаскиваем его значение
                .map(entry -> {
                    List<Order> monthOrders = entry.getValue();
                    BigDecimal totalSum = monthOrders.stream()//его общую сумму
                            .map(Order::getOrderPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    long cancelled = monthOrders.stream()
                            .filter(order -> order.getStatus() == OrderStatus.CANCELED)
                            .count();//колисество отменных заказов фильтруем по статусу заказов

                    return MonthlyOrderStatsDto.builder()
                            .month(entry.getKey().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + year)
                            .totalAmount(totalSum)
                            .totalOrders(monthOrders.size())
                            .cancelledOrders((int) cancelled)
                            .build();// возвращаем обнавленное дто  месяц  с его названием на английском, общую сумму отмененые заказы
                })
                .sorted(Comparator.comparing(dto -> Month.valueOf(dto.getMonth().split(" ")[0].toUpperCase())))
                .toList();//сортируем по компаратору  по месяцу  и в список
    }

    @Override
    public List<DailyOrderStatsDto> getDailyOrders(YearMonth month) {
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        Long restaurantId = currentAdmin.getRestaurant().getId();
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();
        List<Order> orders = orderRepository.findAllByRestaurantIdAndDateBetween(restaurantId, startDate, endDate);

        Map<LocalDate, List<Order>> ordersByDay = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getDate()));
        return ordersByDay.entrySet().stream()
                .map(entry -> {
                    List<Order> dayOrders =  entry.getValue();
                    BigDecimal totalSum = dayOrders.stream()
                            .map(Order::getTotalSum)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    long cancelled = dayOrders.stream()
                            .filter(order -> order.getStatus() == OrderStatus.CANCELED)
                            .count();
                    List<OrderShortInfoDto> shortOrders = dayOrders.stream()
                            .map(order -> OrderShortInfoDto.builder()
                                    .id(order.getId())
                                    .number(order.getNumber())
                                    .totalSum(order.getTotalSum())
                                    .build())
                            .toList();

                    return DailyOrderStatsDto.builder()
                            .date(entry.getKey())
                            .totalAmount(totalSum)
                            .totalOrders(dayOrders.size())
                            .cancelledOrders((int) cancelled)
                            .orders(shortOrders)
                            .build();

                })
                .sorted(Comparator.comparing(dto -> dto.getDate()))
                .toList();
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

        dishToOrderService.createByOrder(cart.getDishToOrders(), order);

        orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return order;
    }

    @Override
    @Transactional
    public void cancelOrderByClient(Long orderId) {
        Client client = clientService.getCurrentClient();
        Order order = getById(orderId);

        if (!order.getClient().getId().equals(client.getId())) {
            throw new AccessDeniedToOrderException(orderId);
        }

        if (order.getStatus() == OrderStatus.COMPLETED ||
                order.getStatus() == OrderStatus.READY_FOR_PICKUP ||
                order.getStatus() == OrderStatus.CANCELED ||
                order.getStatus() == OrderStatus.DELIVERY ) {
            throw new OrderCancelNotAllowedException(order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
    }



    // ========================= PUT ==========================

    @Override
    @Transactional
    public void acceptOrder(Long orderId, Integer cookingTime) {
        Order order = getById(orderId);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be accepted");
        }

        order.setStatus(OrderStatus.PROCESSING);
        order.setDeliveryStatus(DeliveryStatus.ACCEPTED);
        order.setTimeOfAccepted(LocalTime.now().toString());
        order.setCookingTime(cookingTime);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void readyOrder(Long orderId) {
        Order order = getById(orderId);

        if (order.getStatus() != OrderStatus.PROCESSING) {
            throw new IllegalStateException("Only pending orders can be accepted");
        }

        order.setStatus(OrderStatus.READY_FOR_PICKUP);
        order.setDeliveryStatus(DeliveryStatus.READY_FOR_PICKUP);
        order.setEndTimeOfCooking(LocalTime.now().toString());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void completedOrder(Long orderId) {
        Order order = getById(orderId);

        order.setStatus(OrderStatus.COMPLETED);
        order.setDeliveryStatus(DeliveryStatus.COMPLETED);;
        order.setTimeOfDelivery(LocalTime.now().toString());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrderByAdmin(Long orderId) {
        Order order = getById(orderId);

        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    // ========================= DELETE =========================

    @Override
    public void delete(Long orderId) {
        Order order = getById(orderId);
        orderRepository.delete(order);
    }






    // ========================= PRIVATE =========================

    private String generateOrderNumber() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        long countToday = orderRepository.countByDate(startOfDay, endOfDay);
        return String.format("%03d", countToday + 1);
    }

    private RestaurantAdmin getCurrentRestaurantAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        String username = authentication.getName();
        return restaurantAdminRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Admin not found: " + username));
    }


}
