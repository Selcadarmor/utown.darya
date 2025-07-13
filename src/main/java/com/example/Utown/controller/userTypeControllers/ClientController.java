package com.example.Utown.controller.userTypeControllers;


import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.clientDTO.ClientRegistrationDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderRequestDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderResponseDto;
import com.example.Utown.dto.orderDTO.OrderDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.exception.CartNotFoundException;
import com.example.Utown.mapper.OrderMapper;
import com.example.Utown.model.Order;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.service.*;
import com.example.Utown.service.UserType.client.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    private final RestaurantCategoryService restaurantCategoryService;
    private final ClientService clientService;
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

//    @GetMapping("/restaurants")
//    @Operation(summary = "Get all restaurants", description = "Returns restaurants for clients")
//    public ResponseEntity<List<RestaurantForClientDto>> getAllRestaurants() {
//        List<RestaurantForClientDto> restaurants = restaurantService.getAllRestaurantsForClient();
//        return ResponseEntity.ok(restaurants);
//    }
//
//    @GetMapping("/category/{categoryId}")
//    @Operation(summary = "Get all restaurants by Restaurant Category", description = "Get all restaurants by Restaurant Category" )
//    public ResponseEntity<List<RestaurantForClientDto>> getRestaurantsByCategory(@PathVariable Long categoryId) {
//        List<RestaurantForClientDto> result = restaurantService.getRestaurantsByCategoryId(categoryId);
//        return ResponseEntity.ok(result);
//    }
//
//    @GetMapping("/restaurants/fastest")
//    @Operation(summary = "Get restaurants sorted by fastest delivery", description = "Sorted by deliveryTime")
//    public List<RestaurantForClientDto> getRestaurantsSortedByDeliveryTime() {
//        return restaurantService.getAllRestaurantsSortedByDeliveryTime();
//    }

    @PostMapping("/registration_client")  //Passed
    @Operation(summary = "Register Client", description = "Registration for client users")
    public ResponseEntity<String> registerClient(@RequestBody ClientRegistrationDto dto) {
        authService.registration(dto);
        return ResponseEntity.ok("Client registered successfully");
    }

    @PostMapping("/address_create") //Passed
    @Operation(summary = "Add address for current user", description = "Add address to currently authenticated user")
    public ResponseEntity<Void> addAddressForCurrentUser(
            @RequestBody AddressDto addressDto,
            @AuthenticationPrincipal User user
    ) {
        clientService.saveAddressForClient(user.getUsername(), addressDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/address_update")  //Passed
    @Operation(summary = "Update", description = "Update fullname and addresses for current client")
    public ResponseEntity<Void> updateClientProfile(
            @RequestBody ClientProfileUpdateDto dto,
            @AuthenticationPrincipal User user
    ) {
        clientService.updateClientProfile(user.getUsername(), dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/addresses") //Passed
    @Operation(summary = "Get all addresses for current client", description = "Returns addresses for authenticated client")
    public ResponseEntity<List<AddressDto>> getClientAddresses(@AuthenticationPrincipal User user) {
        List<AddressDto> addresses = addressService.getAddressesByClient(user.getUsername());
        return ResponseEntity.ok(addresses);
    }

    @DeleteMapping("/address/{id}") //Passed
    @Operation(summary = "Delete address", description = "Delete address for authenticated client")
    public ResponseEntity<Void> deleteAddressForClient(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        clientService.deleteAddressForCLient(id, user.getUsername());
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