package com.example.Utown.controller.userTypeControllers;


import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryRestaurantProfileDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderRequestDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderResponseDto;
import com.example.Utown.dto.orderDTO.OrderDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantDTO.RestaurantProfileDto;
import com.example.Utown.service.*;
import com.example.Utown.mapper.OrderMapper;
import com.example.Utown.model.Order;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.service.UserType.client.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Tag(name = "Client", description = "Client specific operations")
public class ClientController {

    private final DishCategoryService dishCategoryService;
    private final ClientService clientService;
    private final RestaurantCategoryService restaurantCategoryService;
    private final RestaurantService restaurantService;
    private final AddressService addressService;
    private final AuthService authService;
    private final DishToOrderService dishToOrderService;
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/restaurant_categories") //Passed
    @Operation(summary = "Get all restaurant categories", description = "Restaurant categories with restaurants count for client")
    public ResponseEntity<List<RestaurantCategoryForClient>> getAllCategoriesForClient() {
        List<RestaurantCategoryForClient> categories =
                restaurantCategoryService.getAllCategoriesWithRestaurantCount();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/restaurants") //Passed
    @Operation(summary = "Get all restaurants", description = "Sorted by recommendation")
    public ResponseEntity<Page<RestaurantForClientDto>> getRecommendedRestaurantsForClient(Pageable pageable) {
        Page<RestaurantForClientDto> restaurants = restaurantService.getRecommendedRestaurantsForClient(pageable);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/restaurants_fastest_delivery") //Passed
    @Operation(summary = "Get all restaurants", description = "Sorted by DeliveryTime")
    public ResponseEntity<Page<RestaurantForClientDto>> getFastestDeliveryRestaurantsForClient(Pageable pageable) {
        Page<RestaurantForClientDto> restaurants = restaurantService.getFastestDeliveryRestaurantsForClient(pageable);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/category/{categoryId}") //Passed
    @Operation(summary = "Get all restaurants by Restaurant Category", description = "Get all restaurants by Restaurant Category" )
    public ResponseEntity<Page<RestaurantForClientDto>> getRestaurantsByCategory(@PathVariable Long categoryId, Pageable pageable) {
        Page<RestaurantForClientDto> restaurants = restaurantService.getRestaurantsByCategory(categoryId, pageable);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/restaurants/search")
    @Operation(summary = "Search restaurants", description = "Search restaurants by query with sorting and pagination")
    public ResponseEntity<Page<RestaurantForClientDto>> searchRestaurants(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Page<RestaurantForClientDto> restaurants = restaurantService.searchRestaurants(query, page, size, sortBy, direction);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/profile/{restaurantId}")
    @Operation(summary = "Get restaurant profile", description = "Returns profile info for restaurant")
    public ResponseEntity<RestaurantProfileDto> getRestaurantProfile(@PathVariable Long restaurantId) {
        RestaurantProfileDto profile = restaurantService.getRestaurantProfile(restaurantId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/{restaurantId}/dish_categories")
    @Operation(summary = "Get dish categories for restaurant", description = "Returns all dish categories for a specific restaurant with dish count")
    public ResponseEntity<List<DishCategoryRestaurantProfileDto>> getDishCategoriesByRestaurant(@PathVariable Long restaurantId) {
        List<DishCategoryRestaurantProfileDto> categories = dishCategoryService.getDishCategoriesByRestaurant(restaurantId);
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/address_create") //Passed
    @Operation(summary = "Add address for current user", description = "Add address to currently authenticated user")
    public ResponseEntity<Void> addAddressForCurrentUser(@RequestBody AddressDto addressDto) {
        clientService.saveAddressForClient(addressDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/address_update") //Passed
    @Operation(summary = "Update", description = "Update fullName and default address for current client")
    public ResponseEntity<ClientProfileUpdateDto> updateClientProfile(
            @RequestBody ClientProfileUpdateDto dto
    ) {
        ClientProfileUpdateDto updatedProfile = clientService.updateClientProfile(dto);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/addresses") //Passed
    @Operation(summary = "Get all addresses for current client", description = "Returns addresses for authenticated client")
    public ResponseEntity<List<AddressDto>> getClientAddresses() {
        List<AddressDto> addresses = clientService.getAddressesByClient();
        return ResponseEntity.ok(addresses);
    }

    @DeleteMapping("/address/{id}") //Passed
    @Operation(summary = "Delete address for current client", description = "Deletes an address belonging to the authenticated client")
    public ResponseEntity<Void> deleteAddressForClient(@PathVariable("id") Long addressId) {
        clientService.deleteAddressForCLient(addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cart")
    @Operation(summary = "Get all dishes in cart", description = "Returns all dish items in current client's cart")
    public ResponseEntity<List<DishToOrderResponseDto>> getCartItems(@AuthenticationPrincipal User user) {
        Long cartId = clientService.getCurrentClient().getCart().getId();
        List<DishToOrderResponseDto> items = dishToOrderService.getAllByCartId(cartId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/cart/add")
    @Operation(summary = "Add dish to cart", description = "Adds or updates a dish in the cart")
    public ResponseEntity<Void> addToCart(
            @RequestBody DishToOrderRequestDto dto,
            @AuthenticationPrincipal User user
    ) {
        Client client = clientService.getCurrentClient();

        if (client.getCart() == null || client.getCart().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "У клиента нет корзины или cartId равен null");
        }

        Long cartId = client.getCart().getId();
        dishToOrderService.addToCart(cartId, dto);

        return ResponseEntity.ok().build();
    }


    @PutMapping("/cart/update/{id}")
    @Operation(summary = "Update dish in cart", description = "Updates count and elements for a dish in cart")
    public ResponseEntity<DishToOrderResponseDto> updateCartItem(
            @PathVariable Long id,
            @RequestBody DishToOrderRequestDto dto
    ) {
        return ResponseEntity.ok(dishToOrderService.update(id, dto));
    }

    @DeleteMapping("/cart/delete/{id}")
    @Operation(summary = "Delete dish from cart", description = "Removes a dish from cart")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        dishToOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cart/clear")
    @Operation(summary = "Clear the cart", description = "Removes all dishes from the client's cart")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User user) {
        Client client = clientService.getCurrentClient();

        if (client.getCart() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart not found for client");
        }

        dishToOrderService.clearCart(client.getCart().getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/confirm")
    @Operation(summary = "Confirm order", description = "Creates an order from the current cart and clears the cart")
    public ResponseEntity<OrderDto> confirmOrder(
            @AuthenticationPrincipal User user
    ) {
        Client client = clientService.getCurrentClient();
        Order order = orderService.createOrderFromCart(client);
        OrderDto dto = orderMapper.orderToDto(order);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    @Operation(summary = "Отмена заказа клиентом", description = "Позволяет клиенту отменить свой заказ, если он еще не завершен")
    public ResponseEntity<?> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User user
    ) {
        Client client = clientService.getCurrentClient();
        orderService.cancelOrderByClient(orderId, client.getId());
        return ResponseEntity.ok().build();
    }

}