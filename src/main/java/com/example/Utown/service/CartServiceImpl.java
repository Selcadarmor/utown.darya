package com.example.Utown.service;

import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.CartMapper;
import com.example.Utown.model.Cart;
import com.example.Utown.model.User;
import com.example.Utown.repository.CartRepository;
import com.example.Utown.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    public CartDto createCart(CartDto dto) {
        Cart cart = cartMapper.cartDtoToEntity(dto);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found", dto.getUserId()));
        cart.setUser(user);

        Cart saved = cartRepository.save(cart);
        return cartMapper.cartToDto(saved);
    }

    @Override
    public CartDto getCartById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found", id));
        return cartMapper.cartToDto(cart);
    }

    @Override
    public List<CartDto> getAllCarts() {
        return cartRepository.findAll()
                .stream()
                .map(cartMapper::cartToDto)
                .toList();
    }

    @Override
    public CartDto updateCart(Long id, CartDto dto) {
        Cart existing = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found", id));

        existing.setDeliveryPrice(dto.getDeliveryPrice());
        existing.setSumOrder(dto.getSumOrder());
        existing.setTotalDish(dto.getTotalDish());
        existing.setTotalSum(dto.getTotalSum());

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found", dto.getUserId()));
            existing.setUser(user);
        }

        Cart updated = cartRepository.save(existing);
        return cartMapper.cartToDto(updated);
    }

    @Override
    public void deleteCart(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cart not found", id);
        }
        cartRepository.deleteById(id);
    }
}

