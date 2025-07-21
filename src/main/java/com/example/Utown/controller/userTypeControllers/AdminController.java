package com.example.Utown.controller.userTypeControllers;

import com.example.Utown.dto.clientDTO.ClientDetailsDto;
import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.dto.clientDTO.ClientUpdateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDetailsDto;
import com.example.Utown.dto.dishDTO.DishCreateDto;
import com.example.Utown.dto.dishDTO.DishDetailsDto;
import com.example.Utown.dto.dishDTO.DishInfoDto;
import com.example.Utown.dto.orderDTO.OrderDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.service.DishCategoryService;
import com.example.Utown.service.DishServiceImpl;
import com.example.Utown.service.OrderService;
import com.example.Utown.service.UserType.client.ClientServiceImpl;
import com.example.Utown.service.RestaurantServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * AdminController – контроллер для административной панели.
 * Позволяет администратору управлять клиентами, ресторанами,
 * их блюдами, категориями блюд и заказами через REST API.
 * Основные функции:
 * - Управление клиентами (CRUD)
 * - Управление ресторанами (CRUD)
 * - Управление блюдами ресторанов
 * - Управление категориями блюд ресторанов
 */
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(
        name = "Admin – Users, Restaurants & Menu",
        description = "Admin panel for managing clients, restaurants, dishes, and categories"
)
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ClientServiceImpl clientService;
    private final RestaurantServiceImpl restaurantService;
    private final DishServiceImpl dishService;
    private final DishCategoryService dishCategoryService;
    private final OrderService orderService;

    // --- Клиенты ---

    @Operation(summary = "Get all clients", description = "Returns a list of all registered clients.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "List of clients retrieved successfully"))
    @GetMapping("/clients")
    public ResponseEntity<Page<ClientDetailsDto>> getAllClients(Pageable pageable) {
        Page<ClientDetailsDto> clients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get client by id", description = "Returns a client with the given id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientInfoDto> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @Operation(summary = "Update client by Id", description = "Updates information of an existing client.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PutMapping("/clients/{id}/active")
    public ResponseEntity<Void> updateClientActiveStatus(@PathVariable Long id, @Valid @RequestBody ClientUpdateDto clientUpdateDto) {
        clientService.updateClientActiveStatus(id, clientUpdateDto.getActive());
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Delete client by Id", description = "Removes a client from the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    // --- Рестораны ---

    @Operation(summary = "Get all restaurants", description = "Returns a list of all restaurants.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "List of restaurants retrieved successfully"))
    @GetMapping("/restaurants")
    public ResponseEntity<Page<RestaurantInfoDto>> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RestaurantInfoDto> restaurants = restaurantService.getAllRestaurants(page, size);
        return ResponseEntity.ok(restaurants);
    }

    @Operation(summary = "Get restaurant by id", description = "Returns a restaurant with the given id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantDetailsDto> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantDetails(id));
    }

    @Operation(summary = "Create restaurant", description = "Create Restaurant via the admin panel.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurant created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Duplicate restaurant")
    })
    @PostMapping("/restaurants")
    public ResponseEntity<RestaurantDetailsDto> createRestaurant(
            @Valid @RequestBody RestaurantCreateDto restaurantCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantService.createRestaurant(restaurantCreateDto));
    }

    @Operation(summary = "Update restaurant by Id", description = "Updates restaurant info.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant updated successfully."),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @PutMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantDetailsDto> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantUpdateDto restaurantUpdateDto) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, restaurantUpdateDto));
    }

    @Operation(summary = "Delete restaurant by Id", description = "Deactivates a restaurant in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurant deactivated successfully."),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @DeleteMapping("/restaurants/{id}")
    public void deactivateRestaurant(@PathVariable Long id) {
        restaurantService.deactivateRestaurant(id);
    }

    // --- Блюда ---

    @Operation(summary = "Get all dishes by restaurant ID", description = "Returns a paginated list of dishes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dishes retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("restaurants/{restaurantId}/dishes")
    public ResponseEntity<Page<DishDetailsDto>> getDishesByRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DishDetailsDto> dishes = dishService.getDishesByRestaurantId(restaurantId, page, size);
        return ResponseEntity.ok(dishes);
    }

    @Operation(summary = "Create dish for restaurant", description = "Adds a new dish to the restaurant's menu.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dish created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Restaurant or category not found")
    })
    @PostMapping("/restaurants/{restaurantId}/dishes")
    public ResponseEntity<DishInfoDto> createDishForRestaurant(
            @PathVariable Long restaurantId,
            @Valid @RequestBody DishCreateDto dishCreateDto) {
        DishInfoDto dish = dishService.createDishForRestaurant(restaurantId, dishCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dish);
    }

    @Operation(summary = "Update dish for restaurant", description = "Updates an existing dish.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dish updated successfully."),
            @ApiResponse(responseCode = "404", description = "Dish or restaurant not found")
    })
    @PutMapping("/restaurants/{restaurantId}/dishes/{dishId}")
    public ResponseEntity<DishInfoDto> updateDishForRestaurant(
            @PathVariable Long restaurantId,
            @PathVariable Long dishId,
            @Valid @RequestBody DishCreateDto dishDto) {
        DishInfoDto dish = dishService.updateDishForRestaurant(restaurantId, dishId, dishDto);
        return ResponseEntity.ok(dish);
    }

    // --- Категории блюд ---

    @Operation(summary = "Get all dish categories by restaurant ID", description = "Returns categories for the restaurant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/restaurants/{restaurantId}/dish-categories")
    public ResponseEntity<Page<DishCategoryDetailsDto>> getDishCategoriesByRestaurantId(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DishCategoryDetailsDto> result = dishCategoryService.getDishCategoriesByRestaurantId(restaurantId, page, size);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Create dish category for restaurant", description = "Adds a new dish category.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @PostMapping("/restaurants/{restaurantId}/dish-categories")
    public ResponseEntity<DishCategoryDetailsDto> createDishCategoryForRestaurant(
            @PathVariable Long restaurantId,
            @Valid @RequestBody DishCategoryCreateDto dto) {
        DishCategoryDetailsDto created = dishCategoryService.createDishCategoryForRestaurant(restaurantId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    //история заказов
    @Operation(
            summary = "View restaurant order history",
            description = "Endpoint to get all orders related to a specific restaurant."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Orders not found")
    })
    @GetMapping("/clients/{clientId}/orders")
    public ResponseEntity<Page<OrderDetailsDto>> getOrderDetailsByClient(@PathVariable Long clientId,
                                                                         Pageable pageable) {
        Page<OrderDetailsDto> orders = orderService.getOrderDetailsByClient(clientId, pageable);
        return ResponseEntity.ok(orders);

    }
}
