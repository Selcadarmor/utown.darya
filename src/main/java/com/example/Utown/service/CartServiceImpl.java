package com.example.Utown.service;

import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.model.Cart;
import com.example.Utown.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.Utown.mapper.CartMapper;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Override
    public Cart getCartById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id " + id));
        return cart;
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public Cart createCart(CartDto dto) {
        Cart cart = cartMapper.cartDtoToEntity(dto);
        return cartRepository.save(cart);
    }

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
    public void deleteCart(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new EntityNotFoundException("Cart not found with id " + id);
        }
        cartRepository.deleteById(id);
    } //Поменять на очистку корзины
}

