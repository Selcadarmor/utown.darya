package com.example.Utown.service;

import com.example.Utown.dto.cartDTO.AddToCartRequest;
import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.exception.*;
import com.example.Utown.mapper.DishToOrderMapper;
import com.example.Utown.model.Cart;
import com.example.Utown.model.Dish;
import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.Element;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.CartRepository;
import com.example.Utown.repository.DishRepository;
import com.example.Utown.repository.ElementRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.List;
import com.example.Utown.mapper.CartMapper;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ClientRepository clientRepository;
    private final DishRepository dishRepository;
    private final ElementRepository elementRepository;
    private final DishToOrderMapper dishToOrderMapper;

    public Cart createCart(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException("Клиент не найден"));

        Cart cart = new Cart();
        cart.setClient(client);
        cart.setDeliveryPrice(BigDecimal.ZERO);
        cart.setSumOrder(BigDecimal.ZERO);
        cart.setTotalDish(0);
        cart.setTotalSum(BigDecimal.ZERO);

        cart = cartRepository.save(cart);

        client.setCart(cart);
        clientRepository.save(client);

        return cart;
    }

    @Override
    @Transactional
    public Cart createCart(Long clientId, CartDto dto) {
        // 1. Получаем клиента
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException("Клиент с id " + clientId + " не найден"));

        // 2. Проверяем, есть ли уже корзина
        if (client.getCart() != null) {
            throw new CartAlreadyExistsException("У клиента уже есть корзина");
        }

        // 3. Преобразуем DTO в сущность Cart
        Cart cart = cartMapper.cartDtoToEntity(dto);

        // 4. Привязываем клиента к корзине ❗️
        cart.setClient(client);

        // 5. Инициализируем значения
        if (cart.getDeliveryPrice() == null) cart.setDeliveryPrice(BigDecimal.ZERO);
        if (cart.getTotalDish() == null) cart.setTotalDish(0);
        if (cart.getSumOrder() == null) cart.setSumOrder(BigDecimal.ZERO);
        if (cart.getTotalSum() == null) cart.setTotalSum(BigDecimal.ZERO);

        // 6. Сохраняем корзину
        cart = cartRepository.save(cart);

        // 7. Привязываем корзину к клиенту (обратная сторона связи)
        client.setCart(cart);
        clientRepository.save(client);

        return cart;
    }

    @Override
    public CartDto getCartById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id " + id));
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
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id " + id));

        existingCart.setDeliveryPrice(dto.getDeliveryPrice());
        existingCart.setSumOrder(dto.getSumOrder());
        existingCart.setTotalDish(dto.getTotalDish());
        existingCart.setTotalSum(dto.getTotalSum());

        List<DishToOrder> dishToOrders = dto.getDishToOrder().stream()
                .map(dishToOrderMapper::toEntity)
                .collect(Collectors.toList());
        existingCart.setDishToOrder(dishToOrders);

        return cartRepository.save(existingCart);
    }

    @Override
    public void deleteCart(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new CartNotFoundException("Cart not found with id " + id);
        }
        cartRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addDishToCart(Long clientId, AddToCartRequest request) {
        log.info("Добавляем блюдо в корзину: clientId={}, dishId={}, elementId={}, count={}",
                clientId, request.getDishId(), request.getElementId(), request.getCount());

        // 1. Получаем клиента
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException("Клиент с id " + clientId + " не найден"));
        log.info("Клиент найден: clientId={}", client.getId());

        // 2. Получаем блюдо
        Dish dish = dishRepository.findById(request.getDishId())
                .orElseThrow(() -> new DishNotFoundException(request.getDishId()));
        log.info("Блюдо найдено: dishId={}, title={}", dish.getId(), dish.getTitle());

        // 3. Получаем элемент (если указан)
        final Element element = request.getElementId() != null
                ? elementRepository.findById(request.getElementId())
                .orElseThrow(() -> new ElementNotFoundException(request.getElementId()))
                : null;

        log.info("Элемент: {}", element != null ? element.getId() : "нет");

        // 4. Проверяем/создаём корзину
        Cart cart = client.getCart();
        if (cart == null) {
            log.info("🛒 У клиента нет корзины. Создаём новую...");
            cart = createCart(clientId);
            log.info("Новая корзина создана: cartId={}", cart.getId());
        }

        if (cart.getDishToOrder() == null) {
            cart.setDishToOrder(new ArrayList<>());
        }

        log.info("Обрабатываем добавление блюда в корзину");

        // 5. Проверка на наличие уже добавленного блюда с тем же элементом
        Optional<DishToOrder> existing = cart.getDishToOrder().stream()
                .filter(dto -> dto.getDish().getId().equals(dish.getId()) &&
                        ((dto.getElement() == null && element == null) ||
                                (dto.getElement() != null && dto.getElement().equals(element))))
                .findFirst();

        // 6. Расчёт цены
        BigDecimal elementPrice = element != null ? element.getPrice() : BigDecimal.ZERO;
        BigDecimal unitPrice = dish.getPrice().add(elementPrice);

        // 7. Добавляем или обновляем
        if (existing.isPresent()) {
            DishToOrder dto = existing.get();
            dto.setCount(dto.getCount() + request.getCount());
            dto.setSum(unitPrice.multiply(BigDecimal.valueOf(dto.getCount())));
            log.info("Обновили блюдо в корзине: dishId={}, elementId={}, newCount={}, newSum={}",
                    dish.getId(), element != null ? element.getId() : null, dto.getCount(), dto.getSum());
        } else {
            DishToOrder dto = DishToOrder.builder()
                    .cart(cart)
                    .dish(dish)
                    .element(element)
                    .count(request.getCount())
                    .sum(unitPrice.multiply(BigDecimal.valueOf(request.getCount())))
                    .build();
            cart.getDishToOrder().add(dto);
            log.info("Добавили новое блюдо: dishId={}, elementId={}, count={}, sum={}",
                    dish.getId(), element != null ? element.getId() : null, dto.getCount(), dto.getSum());
        }

        // 8. Пересчёт корзины
        BigDecimal totalSum = cart.getDishToOrder().stream()
                .map(DishToOrder::getSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setSumOrder(totalSum);
        cart.setTotalDish(cart.getDishToOrder().stream().mapToInt(DishToOrder::getCount).sum());
        cart.setTotalSum(totalSum.add(cart.getDeliveryPrice()));

        // 9. Сохраняем
        try {
            cartRepository.save(cart);
            log.info("Корзина сохранена: cartId={}, totalDish={}, sumOrder={}, totalSum={}",
                    cart.getId(), cart.getTotalDish(), cart.getSumOrder(), cart.getTotalSum());
        } catch (Exception e) {
            log.error("Ошибка при сохранении корзины: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public void updateDishInCart(Long clientId, AddToCartRequest request) {
        Cart cart = getClientCart(clientId);

        final Element element = request.getElementId() != null
                ? elementRepository.findById(request.getElementId())
                .orElseThrow(() -> new ElementNotFoundException(request.getElementId()))
                : null;

        // Ищем DishToOrder с таким dishId и тем же элементом
        DishToOrder existing = cart.getDishToOrder().stream()
                .filter(item -> item.getDish().getId().equals(request.getDishId())
                        && ((item.getElement() == null && element == null) ||
                        (item.getElement() != null && item.getElement().equals(element))))
                .findFirst()
                .orElseThrow(() -> new DishNotInCartException(request.getDishId()));

        if (request.getCount() <= 0) {
            cart.getDishToOrder().remove(existing);
            existing.setCart(null);
        } else {
            existing.setCount(request.getCount());

            BigDecimal elementPrice = element != null ? element.getPrice() : BigDecimal.ZERO;
            BigDecimal unitPrice = existing.getDish().getPrice().add(elementPrice);
            existing.setSum(unitPrice.multiply(BigDecimal.valueOf(request.getCount())));
        }

        recalculateCart(cart);
        cartRepository.save(cart);
    }


    @Override
    @Transactional
    public void removeDishFromCart(Long clientId, Long dishId, Long elementId) {
        AddToCartRequest request = new AddToCartRequest();
        request.setDishId(dishId);
        request.setElementId(elementId);
        request.setCount(0);

        updateDishInCart(clientId, request);
    }//не работает пока

    @Transactional
    public void clearCart(Long clientId) {
        Cart cart = getClientCart(clientId);
        cart.getDishToOrder().clear();

        cart.setTotalDish(0);
        cart.setSumOrder(BigDecimal.ZERO);
        cart.setTotalSum(cart.getDeliveryPrice());

        cartRepository.save(cart);
    }

    private void recalculateCart(Cart cart) {
        BigDecimal sum = cart.getDishToOrder().stream()
                .map(DishToOrder::getSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalDish = cart.getDishToOrder().stream()
                .mapToInt(DishToOrder::getCount)
                .sum();

        cart.setSumOrder(sum);
        cart.setTotalDish(totalDish);
        cart.setTotalSum(sum.add(cart.getDeliveryPrice()));
    }

    private Cart getClientCart(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException("Клиент с id " + clientId + " не найден"));

        Cart cart = client.getCart();
        if (cart == null) {
            throw new NoCartForClientException("У клиента нет корзины");
        }

        return cart;
    }

}

