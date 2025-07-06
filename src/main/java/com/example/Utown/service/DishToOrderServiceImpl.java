package com.example.Utown.service;

import com.example.Utown.dto.dishToOrderDTO.DishToOrderDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.DishToOrderMapper;
import com.example.Utown.model.Cart;
import com.example.Utown.model.Dish;
import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.Element;
import com.example.Utown.repository.CartRepository;
import com.example.Utown.repository.DishRepository;
import com.example.Utown.repository.DishToOrderRepository;
import com.example.Utown.repository.ElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishToOrderServiceImpl implements DishToOrderService {

    private final DishToOrderRepository dishToOrderRepository;
    private final CartRepository cartRepository;
    private final DishRepository dishRepository;
    private final DishToOrderMapper mapper;
    private final ElementRepository elementRepository;

    @Override
    public DishToOrder create(DishToOrderDto dto) {
        Cart cart = dto.getCart();
        if (cart == null || cart.getId() == null) {
            throw new ResourceNotFoundException("Cart is required", null);
        }

        Dish dish = dto.getDish();
        if (dish == null || dish.getId() == null) {
            throw new ResourceNotFoundException("Dish is required", null);
        }

        Element element = dto.getElement(); // может быть null
        BigDecimal elementPrice = element != null ? element.getPrice() : BigDecimal.ZERO;

        BigDecimal unitPrice = dish.getPrice().add(elementPrice);
        BigDecimal sum = unitPrice.multiply(BigDecimal.valueOf(dto.getCount()));

        DishToOrder entity = DishToOrder.builder()
                .count(dto.getCount())
                .sum(sum)
                .cart(cart)
                .dish(dish)
                .element(element)
                .build();

        return dishToOrderRepository.save(entity);
    }

    @Override
    public DishToOrderDto getById(Long id) {
        DishToOrder entity = dishToOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishToOrder not found", id));
        return mapper.toDto(entity);
    }

    @Override
    public List<DishToOrderDto> getAll() {
        return dishToOrderRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DishToOrder update(Long id, DishToOrderDto dto) {
        DishToOrder entity = dishToOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishToOrder not found", id));

        Cart cart = dto.getCart();
        if (cart == null || cart.getId() == null) {
            throw new ResourceNotFoundException("Cart is required", null);
        }

        Dish dish = dto.getDish();
        if (dish == null || dish.getId() == null) {
            throw new ResourceNotFoundException("Dish is required", null);
        }

        Element element = dto.getElement(); // может быть null
        BigDecimal elementPrice = element != null ? element.getPrice() : BigDecimal.ZERO;

        BigDecimal unitPrice = dish.getPrice().add(elementPrice);
        BigDecimal sum = unitPrice.multiply(BigDecimal.valueOf(dto.getCount()));

        entity.setCount(dto.getCount());
        entity.setSum(sum);
        entity.setCart(cart);
        entity.setDish(dish);
        entity.setElement(element);

        return dishToOrderRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        DishToOrder entity = dishToOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishToOrder not found", id));
        dishToOrderRepository.delete(entity);
    }
}

