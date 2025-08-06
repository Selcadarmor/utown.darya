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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DishToOrderServiceImpl implements DishToOrderService {

    private final DishToOrderRepository dishToOrderRepository;
    private final CartRepository cartRepository;
    private final DishService dishService;
    private final ElementService elementService;

    // ========================= GET =========================

        @Override
        public DishToOrder getById(Long id) {
            log.info("Fetching DishToOrder by ID: {}", id);
            return dishToOrderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("DishToOrder", id));
        }

        @Override
        public List<DishInCartDto> getDishesInCart(Long cartId) {
            log.info("Fetching dishes in cart with ID: {}", cartId);
            return dishToOrderRepository.findDishesInCartByCartId(cartId);
        }

        @Override
        public List<DishToOrder> getAll() {
            log.info("Fetching all DishToOrder records");
            return dishToOrderRepository.findAll();
        }

        @Override
        public Set<String> getElementNames(Long dishToOrderId) {
            log.info("Fetching element names for DishToOrder ID: {}", dishToOrderId);
            return dishToOrderRepository.findElementNamesByDishToOrderId(dishToOrderId);
        }

        // ========================= POST =========================

        @Override
        @Transactional
        public DishToOrder createByCart(Long cartId, Long dishId, DishToOrderRequestDto dto) {
            log.info("Creating DishToOrder for cart ID: {}, dish ID: {}", cartId, dishId);
            Dish dish = dishService.getDishById(dishId);
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart", cartId));

            BigDecimal elementsPriceSum = elementService.calculateElementsPrice(dto.getSelectedElementIds());
            BigDecimal totalOneItemPrice = dish.getPrice().add(elementsPriceSum);
            BigDecimal totalSum = totalOneItemPrice.multiply(BigDecimal.valueOf(dto.getCount()));

            Set<Element> selectedElements = elementService.getElementsByIds(dto.getSelectedElementIds());

            DishToOrder dishToOrder = DishToOrder.builder()
                    .dish(dish)
                    .cart(cart)
                    .count(dto.getCount())
                    .sum(totalSum)
                    .selectedElements(new HashSet<>(selectedElements))
                    .build();

            DishToOrder saved = dishToOrderRepository.save(dishToOrder);
            log.info("Created DishToOrder ID: {}", saved.getId());
            return saved;
        }

        @Override
        public List<DishToOrder> createByOrder(List<DishToOrder> cartDishToOrders, Order order) {
            log.info("Creating DishToOrder list for order ID: {}", order.getId());

            List<DishToOrder> newItems = cartDishToOrders.stream()
                    .map(oldItem -> DishToOrder.builder()
                            .dish(oldItem.getDish())
                            .count(oldItem.getCount())
                            .sum(oldItem.getSum())
                            .selectedElements(new HashSet<>(oldItem.getSelectedElements()))
                            .order(order)
                            .build())
                    .collect(Collectors.toList());

            List<DishToOrder> savedItems = dishToOrderRepository.saveAll(newItems);
            log.info("Saved {} DishToOrder items for order ID: {}", savedItems.size(), order.getId());
            return savedItems;
        }

        // ========================= PUT =========================

        @Override
        @Transactional
        public void update(Long dishToOrderId, DishToOrderRequestDto dto) {
            log.info("Updating DishToOrder ID: {}", dishToOrderId);
            DishToOrder dishToOrder = getById(dishToOrderId);

            Dish dish = dishToOrder.getDish();

            BigDecimal elementsPriceSum = elementService.calculateElementsPrice(dto.getSelectedElementIds());
            BigDecimal totalOneItemPrice = dish.getPrice().add(elementsPriceSum);
            BigDecimal totalSum = totalOneItemPrice.multiply(BigDecimal.valueOf(dto.getCount()));

            Set<Element> selectedElements = elementService.getElementsByIds(dto.getSelectedElementIds());

            dishToOrder.setCount(dto.getCount());
            dishToOrder.setSelectedElements(selectedElements);
            dishToOrder.setSum(totalSum);

            dishToOrderRepository.save(dishToOrder);
            log.info("Updated DishToOrder ID: {}", dishToOrderId);
        }

        // ========================= DELETE =========================

        @Override
        @Transactional
        public void delete(Long dishToOrderId) {
            log.info("Deleting DishToOrder ID: {}", dishToOrderId);
            DishToOrder dishToOrder = getById(dishToOrderId);
            dishToOrderRepository.delete(dishToOrder);
            log.info("Deleted DishToOrder ID: {}", dishToOrderId);
        }


}
