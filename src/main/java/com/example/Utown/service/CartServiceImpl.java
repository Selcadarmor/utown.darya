package com.example.Utown.service;

import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.model.Cart;
import com.example.Utown.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.Utown.mapper.CartMapper;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Override
    public Cart createCart(CartDto dto) {
        Cart cart = cartMapper.cartDtoToEntity(dto);
        return cartRepository.save(cart);
    }

    @Override
    public CartDto getCartById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id " + id));
        return cartMapper.cartToDto(cart);
    }

    @Override
    public List<CartDto> getAllCarts() {
        return cartRepository.findAll()
                .stream()
                .map(cartMapper::cartToDto)
                .collect(Collectors.toList());
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
        existingCart.setDish(dto.getDish());

        return cartRepository.save(existingCart);
    }

    @Override
    public void deleteCart(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new EntityNotFoundException("Cart not found with id " + id);
        }
        cartRepository.deleteById(id);
    }
}

