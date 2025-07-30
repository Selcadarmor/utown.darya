package com.example.Utown.service;

import com.example.Utown.dto.dishToOrderDTO.DishToOrderRequestDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderResponseDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishToOrderServiceImpl implements DishToOrderService {

    private final DishToOrderRepository dishToOrderRepository;
    private final DishRepository dishRepository;
    private final DishService dishService;
    private final DishToOrderMapper mapper;
    private final CartRepository cartRepository;
    private final ElementService elementService;
    private final ElementRepository elementRepository;

    // ========================= GET =========================

    @Override
    public DishToOrder getById(Long id) {
        DishToOrder dishToOrder = dishToOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishToOrder", id));
        return dishToOrder;
    }

    @Override
    public List<DishToOrder> getAll() {
        return dishToOrderRepository.findAll();
    }

    // ========================= POST =========================

    @Override
    @Transactional
    public DishToOrder create(Long cartId, Long dishId, DishToOrderRequestDto dto) {
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
    public DishToOrderResponseDto update(Long id, DishToOrderRequestDto dto) {
        DishToOrder entity = dishToOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishToOrder not found", id));

        Dish dish = dishService.getDishById(entity.getDish().getId());

        List<Element> selectedElements = elementRepository.findAllById(dto.getSelectedElementIds());

        entity.setDish(dish);
        entity.setCount(dto.getCount());
        entity.setSelectedElements(selectedElements);
        entity.setSum(dish.getPrice().multiply(BigDecimal.valueOf(dto.getCount())));

        DishToOrder updated = dishToOrderRepository.save(entity);

        // ✅ Пересчёт корзины
        Cart cart = entity.getCart();
        recalculateCart(cart);

        return mapper.toResponseDto(updated);
    }

    // ========================= DELETE =========================

    @Override
    public void delete(Long id) {
        DishToOrder entity = dishToOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishToOrder not found", id));

        Cart cart = entity.getCart(); // получаем корзину ДО удаления

        dishToOrderRepository.delete(entity);

        recalculateCart(cart); // пересчёт корзины после удаления
    }

    @Override
    public List<DishToOrderResponseDto> getAllByCartId(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found", cartId));

        return cart.getDishToOrders().stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    private boolean elementsEqual(List<Element> a, List<Element> b) {
        if (a.size() != b.size()) return false;

        List<Long> aIds = a.stream().map(Element::getId).sorted().toList();
        List<Long> bIds = b.stream().map(Element::getId).sorted().toList();

        return aIds.equals(bIds);
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
