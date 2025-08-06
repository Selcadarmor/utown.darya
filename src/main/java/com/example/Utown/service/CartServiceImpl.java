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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final AddressService addressService;
    private final ClientRepository clientRepository;
    private final DishToOrderService dishToOrderService;
    private final DishService dishService;

    // ========================= GET =========================

    @Override
    public Cart getCartById(Long id) {
        log.debug("Fetching cart by id: {}", id);
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cart not found with id: {}", id);
                    return new ResourceNotFoundException("Cart", id);
                });
        log.debug("Found cart: {}", cart);
        return cart;
    }

    @Override
    public CartDto getCart() {
        Client client = getCurrentClient();
        Cart cart = client.getCart();

        log.info("Getting cart for client username: {}", client.getUsername());

        List<DishInCartDto> dishes = dishToOrderService.getDishesInCart(cart.getId());

        log.debug("Cart id {} contains {} dishes", cart.getId(), dishes.size());

        return new CartDto(
                dishes,
                cart.getDeliveryPrice(),
                cart.getTotalSum()
        );
    }

    @Override
    @Transactional
    public Cart createCart() {
        log.info("Creating new empty cart");
        Cart cart = Cart.builder()
                .deliveryPrice(BigDecimal.ZERO)
                .sumOrder(BigDecimal.ZERO)
                .totalDish(0)
                .totalSum(BigDecimal.ZERO)
                .dishToOrders(new ArrayList<>())
                .build();

        Cart savedCart = cartRepository.save(cart);
        log.info("Created cart with id: {}", savedCart.getId());
        return savedCart;
    }

    @Override
    @Transactional
    public DishToOrder addDishToCart(Long dishId, DishToOrderRequestDto dto) {
        Client client = getCurrentClient();
        Cart cart = getCartById(client.getCart().getId());
        log.info("Adding dish id {} to cart id {} for client {}", dishId, cart.getId(), client.getUsername());

        Dish dish = dishService.getDishById(dishId);

        log.debug("Validating dish restaurant for cart id {}", cart.getId());
        validateDishBelongsToSameRestaurant(cart, dish);

        DishToOrder dishToOrder = dishToOrderService.createByCart(cart.getId(), dishId, dto);
        cart.getDishToOrders().add(dishToOrder);

        log.debug("Recalculating cart totals after adding dish");
        recalculateCart(cart);

        log.info("Dish id {} added to cart id {}", dishId, cart.getId());
        return dishToOrder;
    }

    @Override
    @Transactional
    public CartDto updateCart(Long dishToOrderId, DishToOrderRequestDto dto) {
        Client client = getCurrentClient();
        Cart cart = getCartById(client.getCart().getId());

        log.info("Updating dishToOrder id {} in cart id {}", dishToOrderId, cart.getId());

        dishToOrderService.update(dishToOrderId, dto);

        log.debug("Recalculating cart after update");
        recalculateCart(cart);

        return getCart();
    }

    @Override
    @Transactional
    public CartDto removeDishFromCart(Long dishToOrderId) {
        DishToOrder dishToOrder = dishToOrderService.getById(dishToOrderId);
        Client client = getCurrentClient();
        Cart cart = getCartById(client.getCart().getId());

        log.info("Removing dishToOrder id {} from cart id {}", dishToOrderId, cart.getId());

        cart.getDishToOrders().remove(dishToOrder);

        dishToOrderService.delete(dishToOrderId);

        log.debug("Recalculating cart after dish removal");
        recalculateCart(cart);

        return getCart();
    }

    @Override
    @Transactional
    public CartDto clearCart(Long cartId) {
        Cart cart = getCartById(cartId);
        log.info("Clearing all dishes from cart id {}", cartId);

        cart.getDishToOrders().clear();

        log.debug("Recalculating cart after clearing");
        recalculateCart(cart);

        return getCart();
    }

    // ========================= PRIVATE =========================

    private Client getCurrentClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthenticated access attempt to get current client");
            throw new RuntimeException("User is not authenticated");
        }

        String username = authentication.getName();
        Client client = clientRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Client not found with username: {}", username);
                    return new UsernameNotFoundException("Client not found: " + username);
                });

        log.debug("Current authenticated client: {}", username);
        return client;
    }

    private void recalculateCart(Cart cart) {
        log.info("Recalculating cart totals for cartId: {}", cart.getId());

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

        log.info("Cart recalculated: sumOrder={}, deliveryPrice={}, totalDish={}, totalSum={}",
                sumOrder, deliveryPrice, totalDish, totalSum);
    }

    private BigDecimal calculateDeliveryPrice(Cart cart) {
        log.debug("Calculating delivery price for cartId: {}", cart.getId());

        Client client = cart.getClient();
        if (client.getDefaultAddress() == null) {
            log.warn("Client {} has no default address", client.getId());
            throw new DefaultAddressNotSetException();
        }

        Address defaultAddress = addressService.getAddressById(client.getDefaultAddress());

        List<DishToOrder> dishToOrders = cart.getDishToOrders();
        if (dishToOrders == null || dishToOrders.isEmpty()) {
            log.info("Cart {} is empty. Delivery price is zero.", cart.getId());
            return BigDecimal.ZERO;
        }

        Dish anyDish = dishToOrders.get(0).getDish(); // one restaurant only
        Restaurant restaurant = anyDish.getRestaurant();
        Address restaurantAddress = restaurant.getAddress();

        if (!restaurantAddress.getState().equals(defaultAddress.getState())) {
            log.warn("Delivery not available: different states for restaurant {} and client {}",
                    restaurant.getId(), client.getId());
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
            log.warn("No delivery option available for address: {}", defaultAddress.getFullAddress());
            throw new RuntimeException("No delivery option available");
        }
        if (prices.size() > 1) {
            log.warn("Multiple delivery options found for same area: {}", defaultAddress.getFullAddress());
            throw new RuntimeException("More than one delivery option found for same area");
        }

        log.info("Delivery price for cart {} is {}", cart.getId(), prices.get(0));
        return prices.get(0);
    }

    private void validateDishBelongsToSameRestaurant(Cart cart, Dish newDish) {
        List<DishToOrder> existingDishToOrders = cart.getDishToOrders();

        if (!existingDishToOrders.isEmpty()) {
            Restaurant existingRestaurant = existingDishToOrders.get(0).getDish().getRestaurant();
            Restaurant newDishRestaurant = newDish.getRestaurant();

            if (!existingRestaurant.getId().equals(newDishRestaurant.getId())) {
                log.warn("Attempt to add dish from different restaurant. Existing: {}, New: {}",
                        existingRestaurant.getId(), newDishRestaurant.getId());
                throw new DifferentRestaurantException();
            }
        }

        log.debug("Dish {} belongs to the same restaurant as existing dishes in cart {}", newDish.getId(), cart.getId());
    }

}

