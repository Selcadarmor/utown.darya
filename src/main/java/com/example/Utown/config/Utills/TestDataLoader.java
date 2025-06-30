package com.example.Utown.config.Utills;

import com.example.Utown.model.*;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.repository.*;
import com.example.Utown.repository.UserType.ClientRepository;
import com.example.Utown.repository.UserType.RestaurantAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {

    private final RestaurantCategoryRepository restaurantCategoryRepo;
    private final AddressRepository addressRepo;
    private final RestaurantRepository restaurantRepo;
    private final RestaurantAdminRepository restaurantAdminRepo;
    private final DishCategoryRepository dishCategoryRepo;
    private final DishRepository dishRepo;
    private final OptionRepository optionRepo;
    private final ClientRepository clientRepo;
    private final OrderRepository orderRepo;
    private final DishToOrderRepository dishToOrderRepo;
    private final OperatingModeRepository operatingModeRepo;

    @Override
    public void run(String... args) throws Exception {
        // 1. Category
        RestaurantCategory category = new RestaurantCategory("Корейская кухня", 1, null);
        restaurantCategoryRepo.save(category);

        // 2. Address
        Address address = new Address("Пусан", "Хэундэ");
        addressRepo.save(address);

        // 3. Restaurant
        Restaurant restaurant = Restaurant.builder()
                .title("Korean BBQ House")
                .description("Лучшее барбекю в Корее")
                .phone("010-1234-5678")
                .category(category)
                .minOrderAmount(BigDecimal.valueOf(12000))
                .address(address)
                .fileInfo(null)
                .build();
        restaurantRepo.save(restaurant);

        // 4. Admin
        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setFullName("Ким Менеджер");
        admin.setPhone("010-8888-9999");
        admin.setRestaurant(restaurant);
        restaurantAdminRepo.save(admin);

        // 5. OperatingMode
        OperatingMode mode = new OperatingMode(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(22, 0), restaurant);
        operatingModeRepo.save(mode);

        // 6. Dish category
        DishCategory dishCategory = new DishCategory("Основные блюда", 1, null);
        dishCategoryRepo.save(dishCategory);

        // 7. Dish
        Dish dish = new Dish();
        dish.setTitle("Самгёпсаль");
        dish.setDescription("Свиная грудинка на гриле");
        dish.setPrice(9000);
        dish.setPriority(1);
        dish.setCategory(dishCategory);
        dish.setRestaurant(restaurant);
        dish.setFileInfo(null);
        dishRepo.save(dish);

        // 8. Dish options
        Option opt1 = new Option("Очень остро", 500, dish);
        Option opt2 = new Option("Добавить сыр", 700, dish);
        optionRepo.saveAll(List.of(opt1, opt2));

        // 9. Client
        Client client = new Client();
        client.setFullName("Иван Иванов");
        client.setPhone("010-5555-6666");
        client.setDefaultAddress(address);
        clientRepo.save(client);

        // 10. Order
        Order order = new Order();
        order.setClient(client);
        order.setRestaurant(restaurant);
        order.setRestaurantAdmin(admin);
        order.setTotalAmount(BigDecimal.valueOf(18000));
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);
        order.setOrderType(OrderType.DELIVERY);
        orderRepo.save(order);

        // 11. Order item
        DishToOrder orderItem = new DishToOrder();
        orderItem.setMenuItem(dish);
        orderItem.setQuantity(2);
        orderItem.setPricePerItem(BigDecimal.valueOf(9000));
        orderItem.setTotalPrice(BigDecimal.valueOf(18000));
        orderItem.setOrder(order);
        dishToOrderRepo.save(orderItem);
    }
}
    }