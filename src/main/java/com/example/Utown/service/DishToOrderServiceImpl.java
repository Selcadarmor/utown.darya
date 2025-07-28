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
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishToOrderServiceImpl implements DishToOrderService {

    private final DishToOrderRepository dishToOrderRepository;
    private final CartRepository cartRepository;
    private final DishRepository dishRepository;
    private final DishToOrderMapper mapper;
    private final ElementRepository elementRepository;

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

    @Override
    public DishToOrder create(Long cartId, DishToOrderRequestDto dto) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found", cartId));

        Dish dish = dishRepository.findById(dto.getDishId())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found", dto.getDishId()));

        List<Element> selectedElements = elementRepository.findAllById(dto.getSelectedElementIds());

        BigDecimal sum = dish.getPrice().multiply(BigDecimal.valueOf(dto.getCount()));

        DishToOrder entity = DishToOrder.builder()
                .dish(dish)
                .cart(cart)
                .count(dto.getCount())
                .sum(sum)
                .selectedElements(selectedElements)
                .build();

        return dishToOrderRepository.save(entity);
    }

    @Override
    public DishToOrderResponseDto update(Long id, DishToOrderRequestDto dto) {
        DishToOrder entity = dishToOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishToOrder not found", id));

        Dish dish = dishRepository.findById(dto.getDishId())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found", dto.getDishId()));

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

    @Override
    public void delete(Long id) {
        DishToOrder entity = dishToOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishToOrder not found", id));

        Cart cart = entity.getCart(); // получаем корзину ДО удаления

        dishToOrderRepository.delete(entity);

        recalculateCart(cart); // пересчёт корзины после удаления
    }

    @Override
    public void addToCart(Long cartId, DishToOrderRequestDto dto) {
        // ✅ Проверки входных данных
        if (cartId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart ID must not be null");
        }

        if (dto.getDishId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish ID must not be null");
        }

        if (dto.getSelectedElementIds() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selected element IDs must not be null");
        }

        if (dto.getSelectedElementIds().contains(null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selected element IDs contain null");
        }

        if (dto.getCount() == null || dto.getCount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Count must be greater than 0");
        }

        // ✅ Загрузка сущностей
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found", cartId));

        Dish dish = dishRepository.findById(dto.getDishId())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found", dto.getDishId()));

        List<Element> selectedElements = elementRepository.findAllById(dto.getSelectedElementIds());

        // ✅ Проверка на совпадение существующего блюда с элементами
        for (DishToOrder existing : cart.getDishToOrders()) {
            if (existing.getDish().getId().equals(dish.getId()) &&
                    elementsEqual(existing.getSelectedElements(), selectedElements)) {

                int newCount = existing.getCount() + dto.getCount();
                existing.setCount(newCount);
                existing.setSum(dish.getPrice().multiply(BigDecimal.valueOf(newCount)));

                dishToOrderRepository.save(existing);
                recalculateCart(cart);
                return;
            }
        }

        // ✅ Создание новой позиции
        DishToOrder newItem = DishToOrder.builder()
                .dish(dish)
                .cart(cart)
                .count(dto.getCount())
                .sum(dish.getPrice().multiply(BigDecimal.valueOf(dto.getCount())))
                .selectedElements(selectedElements)
                .build();

        dishToOrderRepository.save(newItem);
        cart.getDishToOrders().add(newItem);

        recalculateCart(cart);
    }

    @Override
    public List<DishToOrderResponseDto> getAllByCartId(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found", cartId));

        return cart.getDishToOrders().stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found", cartId));

        List<DishToOrder> items = cart.getDishToOrders();

        dishToOrderRepository.deleteAll(items);
        items.clear(); // очищаем список в памяти

        recalculateCart(cart); // чтобы обнулить totalDish и totalSum
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
