package com.example.Utown.service;

import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.dto.dishToOrderDTO.DishInCartDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderRequestDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Cart;
import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.CartRepository;
import com.example.Utown.repository.DishToOrderRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ClientRepository clientRepository;
    private final DishToOrderService dishToOrderService;

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

    // ========================= PUT =========================

    @Override
    @Transactional
    public DishToOrder addDishToCart(Long dishId,DishToOrderRequestDto dto) {
        Client client = getCurrentClient();
        Cart cart = getCartById(client.getCart().getId());
        DishToOrder dishToOrder = dishToOrderService.create(cart.getId(),dishId, dto);

        cart.getDishToOrders().add(dishToOrder);
        recalculateCart(cart);

        return dishToOrder;
    }

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
        dishToOrderService.delete(dishToOrderId);

        Client client = getCurrentClient();
        Cart cart = getCartById(client.getCart().getId());

        recalculateCart(cart);
        return getCart();
    }

    @Override
    @Transactional
    public CartDto clearCart(Long cartId) {
        Cart cart = getCartById(cartId);

        List<DishToOrder> dishes = cart.getDishToOrders();
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

        BigDecimal deliveryPrice = cart.getDeliveryPrice() != null ? cart.getDeliveryPrice() : BigDecimal.ZERO;
        BigDecimal totalSum = sumOrder.add(deliveryPrice);

        cart.setSumOrder(sumOrder);
        cart.setTotalDish(totalDish);
        cart.setTotalSum(totalSum);

        cartRepository.save(cart);
    }


}

