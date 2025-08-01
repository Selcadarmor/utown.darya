package com.example.Utown.service;

import com.example.Utown.dto.dishToOrderDTO.DishInCartDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderRequestDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Cart;
import com.example.Utown.model.Dish;
import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.Element;
import com.example.Utown.model.Order;
import com.example.Utown.repository.CartRepository;
import com.example.Utown.repository.DishToOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishToOrderServiceImpl implements DishToOrderService {

    private final DishToOrderRepository dishToOrderRepository;
    private final DishService dishService;
    private final CartRepository cartRepository;
    private final ElementService elementService;

    // ========================= GET =========================

    @Override
    public DishToOrder getById(Long id) {
        DishToOrder dishToOrder = dishToOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishToOrder", id));
        return dishToOrder;
    }

    @Override
    public List<DishInCartDto> getDishesInCart(Long cartId) {
        return dishToOrderRepository.findDishesInCartByCartId(cartId);
    }

    @Override
    public List<DishToOrder> getAll() {
        return dishToOrderRepository.findAll();
    }

    // ========================= POST =========================

    @Override
    @Transactional
    public DishToOrder createByCart(Long cartId, Long dishId, DishToOrderRequestDto dto) {
        Dish dish = dishService.getDishById(dishId);
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", cartId));

        BigDecimal elementsPriceSum = elementService.calculateElementsPrice(dto.getSelectedElementIds());
        BigDecimal totalOneItemPrice = dish.getPrice().add(elementsPriceSum);
        BigDecimal totalSum = totalOneItemPrice.multiply(BigDecimal.valueOf(dto.getCount()));

        List<Element> selectedElements = elementService.getElementsByIds(dto.getSelectedElementIds());

        DishToOrder dishToOrder = DishToOrder.builder()
                .dish(dish)
                .cart(cart)
                .count(dto.getCount())
                .sum(totalSum)
                .selectedElements(selectedElements)
                .build();

        return dishToOrderRepository.save(dishToOrder);
    }



    // ========================= PUT =========================

    @Override
    @Transactional
    public void update(Long dishToOrderId, DishToOrderRequestDto dto) {
        DishToOrder dishToOrder = getById(dishToOrderId);

        Dish dish = dishToOrder.getDish();

        BigDecimal elementsPriceSum = elementService.calculateElementsPrice(dto.getSelectedElementIds());
        BigDecimal totalOneItemPrice = dish.getPrice().add(elementsPriceSum);
        BigDecimal totalSum = totalOneItemPrice.multiply(BigDecimal.valueOf(dto.getCount()));

        List<Element> selectedElements = elementService.getElementsByIds(dto.getSelectedElementIds());

        dishToOrder.setCount(dto.getCount());
        dishToOrder.setSelectedElements(selectedElements);
        dishToOrder.setSum(totalSum);

        dishToOrderRepository.save(dishToOrder);
    }


    // ========================= DELETE =========================

    @Override
    @Transactional
    public void delete(Long dishToOrderId) {
        DishToOrder dishToOrder = getById(dishToOrderId);
        dishToOrderRepository.delete(dishToOrder);
    }

    @Override
    @Transactional
    public void deleteAll(List<DishToOrder> dishes) {
        dishToOrderRepository.deleteAll(dishes);
    }


}
