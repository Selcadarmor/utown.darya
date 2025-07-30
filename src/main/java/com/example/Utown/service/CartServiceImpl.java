package com.example.Utown.service;

import com.example.Utown.dto.cartDTO.CartDto;
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
    public Cart updateCart(Long id, CartDto dto) {
        Cart existingCart = cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id " + id));

        // Обновляем поля (примерно, можно заменить на маппинг из DTO)
        existingCart.setDeliveryPrice(dto.getDeliveryPrice());
        existingCart.setSumOrder(dto.getSumOrder());
        existingCart.setTotalDish(dto.getTotalDish());
        existingCart.setTotalSum(dto.getTotalSum());
        existingCart.setDishToOrders(dto.getDishToOrders());

        return cartRepository.save(existingCart);
    }

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
    // ========================= DELETE =========================

    private final DishToOrderRepository dishToOrderRepository;

    @Override
    public void clearCart(Long cartId) {
        Cart cart = getCartById(cartId);

        List<DishToOrder> items = cart.getDishToOrders();

        dishToOrderRepository.deleteAll(items);
        items.clear(); // очищаем список в памяти

        recalculateCart(cart); // чтобы обнулить totalDish и totalSum
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

