package com.example.Utown.service;

import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.dto.dishToOrderDTO.DishInCartDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderRequestDto;
import com.example.Utown.exception.DefaultAddressNotSetException;
import com.example.Utown.exception.DeliveryNotAvailableException;
import com.example.Utown.exception.DifferentRestaurantException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Address;
import com.example.Utown.model.Cart;
import com.example.Utown.model.Delivery;
import com.example.Utown.model.Dish;
import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.CartRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ClientRepository clientRepository;
    private final DishToOrderService dishToOrderService;
    private final DishService dishService;
    private final AddressService addressService;

    // ========================= GET =========================

    @Override
    public Cart getCartById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", id));
        return cart;
    }

    @Override
    public CartDto getCart() {
        Client client = getCurrentClient();
        Cart cart = client.getCart();

        List<DishInCartDto> dishes = dishToOrderService.getDishesInCart(cart.getId());

        return new CartDto(
                dishes,
                cart.getDeliveryPrice(),
                cart.getTotalSum()
        );
    }

    // ========================= POST =========================

    @Override
    @Transactional
    public Cart createCart() {
        Cart cart = Cart.builder()
                .deliveryPrice(BigDecimal.ZERO)
                .sumOrder(BigDecimal.ZERO)
                .totalDish(0)
                .totalSum(BigDecimal.ZERO)
                .dishToOrders(new ArrayList<>())
                .build();

        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public DishToOrder addDishToCart(Long dishId, DishToOrderRequestDto dto) {
        Client client = getCurrentClient();
        Cart cart = getCartById(client.getCart().getId());
        Dish dish = dishService.getDishById(dishId);

        validateDishBelongsToSameRestaurant(cart, dish);

        DishToOrder dishToOrder = dishToOrderService.createByCart(cart.getId(), dishId, dto);
        cart.getDishToOrders().add(dishToOrder);
        recalculateCart(cart);

        return dishToOrder;
    }


    // ========================= PUT =========================

    @Override
    public CartDto updateCart( Long dishToOrderId, DishToOrderRequestDto dto) {
        Client client = getCurrentClient();
        Cart cart = getCartById(client.getCart().getId());

        dishToOrderService.update(dishToOrderId, dto);
        recalculateCart(cart);
        return getCart();
    }

    // ========================= DELETE =========================

    @Override
    @Transactional
    public CartDto removeDishFromCart(Long dishToOrderId) {
        DishToOrder dishToOrder = dishToOrderService.getById(dishToOrderId);
        Client client = getCurrentClient();
        Cart cart = getCartById(client.getCart().getId());

        cart.getDishToOrders().remove(dishToOrder);

        dishToOrderService.delete(dishToOrderId);

        recalculateCart(cart);
        return getCart();
    }

    @Override
    @Transactional
    public CartDto clearCart(Long cartId) {
        Cart cart = getCartById(cartId);

        List<DishToOrder> dishes = cart.getDishToOrders();
        cart.getDishToOrders().clear();
        dishToOrderService.deleteAll(dishes);

        recalculateCart(cart);

        return getCart();
    }

    // ========================= PRIVATE =========================

    private Client getCurrentClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        String username = authentication.getName();
        return clientRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found: " + username));
    }

    private void recalculateCart(Cart cart) {
        List<DishToOrder> items = cart.getDishToOrders();

        BigDecimal sumOrder = items.stream()
                .map(DishToOrder::getSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalDish = items.stream()
                .mapToInt(DishToOrder::getCount)
                .sum();

        BigDecimal deliveryPrice = calculateDeliveryPrice(cart);
        BigDecimal totalSum = sumOrder.add(deliveryPrice);

        cart.setSumOrder(sumOrder);
        cart.setDeliveryPrice(deliveryPrice);
        cart.setTotalDish(totalDish);
        cart.setTotalSum(totalSum);

        cartRepository.save(cart);
    }

    private BigDecimal calculateDeliveryPrice(Cart cart) {
        Client client = cart.getClient();
        if (client.getDefaultAddress() == null) {
            throw new DefaultAddressNotSetException();
        }

        Address defaultAddress = addressService.getAddressById(client.getDefaultAddress());

        List<DishToOrder> dishToOrders = cart.getDishToOrders();
        if (dishToOrders == null || dishToOrders.isEmpty()) {
            return BigDecimal.ZERO;
        }

        Dish anyDish = dishToOrders.get(0).getDish(); //Cart can have dishes from the same restaurant
        Restaurant restaurant = anyDish.getRestaurant();
        Address restaurantAddress = restaurant.getAddress();

        if (!restaurantAddress.getState().equals(defaultAddress.getState())) {
            throw new DeliveryNotAvailableException(defaultAddress.getFullAddress());
        }

        List<BigDecimal> prices = restaurant.getDeliveries().stream()
                .filter(delivery ->
                        delivery.getDistrict().equals(defaultAddress.getCity()) &&
                                delivery.getArea().equals(defaultAddress.getArea())
                )
                .map(Delivery::getPrice)
                .collect(Collectors.toList());

        if (prices.isEmpty()) {
            throw new RuntimeException("No delivery option available");
        }
        if (prices.size() > 1) {
            throw new RuntimeException("More than one delivery option found for same area");
        }

        return prices.get(0);

    }

    private void validateDishBelongsToSameRestaurant(Cart cart, Dish newDish) {
        List<DishToOrder> existingDishToOrders = cart.getDishToOrders();

        if (!existingDishToOrders.isEmpty()) {
            Restaurant existingRestaurant = existingDishToOrders.get(0).getDish().getRestaurant();
            Restaurant newDishRestaurant = newDish.getRestaurant();

            if (!existingRestaurant.getId().equals(newDishRestaurant.getId())) {
                throw new DifferentRestaurantException();
            }
        }
    }



}

