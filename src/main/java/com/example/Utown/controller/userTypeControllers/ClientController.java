package com.example.Utown.controller.userTypeControllers;


import com.example.Utown.dto.cartDTO.AddToCartRequest;
import com.example.Utown.dto.clientDTO.ClientChangePasswordDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.dto.clientDTO.RestaurantForClientDto;
import com.example.Utown.service.CartService;
import com.example.Utown.service.RestaurantCategoryService;
import com.example.Utown.service.UserType.client.ClientService;
import com.example.Utown.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Tag(name = "Client", description = "Client specific operations")
public class ClientController {

    private final RestaurantCategoryService restaurantCategoryService;
    private final ClientService clientService;
    private final RestaurantService restaurantService;
    private final CartService cartService;

    @GetMapping("/restaurant_categories")
    @Operation(summary = "Get all restaurant categories", description = "Restaurant categories with restaurants count for client")
    public ResponseEntity<List<RestaurantCategoryForClient>> getAllCategoriesForClient() {
        List<RestaurantCategoryForClient> categories =
                restaurantCategoryService.getAllCategoriesWithRestaurantCount();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/restaurants")
    @Operation(summary = "Get all restaurants", description = "Returns restaurants for clients")
    public ResponseEntity<List<RestaurantForClientDto>> getAllRestaurants() {
        List<RestaurantForClientDto> restaurants = restaurantService.getAllRestaurantsForClient();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get all restaurants by Restaurant Category", description = "Get all restaurants by Restaurant Category" )
    public ResponseEntity<List<RestaurantForClientDto>> getRestaurantsByCategory(@PathVariable Long categoryId) {
        List<RestaurantForClientDto> result = restaurantService.getRestaurantsByCategoryId(categoryId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/restaurants/fastest")
    @Operation(summary = "Get restaurants sorted by fastest delivery", description = "Sorted by deliveryTime")
    public List<RestaurantForClientDto> getRestaurantsSortedByDeliveryTime() {
        return restaurantService.getAllRestaurantsSortedByDeliveryTime();
    }

    @PutMapping("/{clientId}/profile_update")
    @Operation(summary = "Update client profile with multiple addresses")
    public ResponseEntity<String> updateClientProfile(
            @PathVariable Long clientId,
            @RequestBody ClientProfileUpdateDto request
    ) {
        clientService.updateClientProfile(clientId, request.getFullName(), request.getAddresses());
        return ResponseEntity.ok("Client profile updated successfully");
    }

    @PutMapping("/change_password")//Passed
    @Operation(summary = "Change client password", description = "")
    public ResponseEntity<String> changePassword(
            @RequestBody @Valid ClientChangePasswordDto dto,
            @AuthenticationPrincipal User user
    ) {
        clientService.changePassword(user.getUsername(), dto);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/{clientId}/cart/add")
    public ResponseEntity<String> addToCart(
            @PathVariable Long clientId,
            @RequestBody AddToCartRequest request
    ) {
        System.out.println("AddToCartRequest received: " + request);

        cartService.addDishToCart(clientId, request);
        return ResponseEntity.ok("Блюдо добавлено в корзину");
    }

    @PutMapping("/{clientId}/cart/update")
    @Operation(summary = "Обновить количество блюда в корзине")
    public ResponseEntity<String> updateDishInCart(
            @PathVariable Long clientId,
            @RequestBody AddToCartRequest request
    ) {
        cartService.updateDishInCart(clientId, request);
        return ResponseEntity.ok("Количество блюда в корзине обновлено");
    }

    @DeleteMapping("/{clientId}/cart/clear")
    @Operation(summary = "Очистить корзину клиента")
    public ResponseEntity<String> clearCart(@PathVariable Long clientId) {
        cartService.clearCart(clientId);
        return ResponseEntity.ok("Корзина успешно очищена");
    }

    @DeleteMapping("/{clientId}/cart/remove")
    public ResponseEntity<?> removeDishFromCart(
            @PathVariable Long clientId,
            @RequestParam Long dishId,
            @RequestParam(name = "elementId", required = false) Long elementId) {

        cartService.removeDishFromCart(clientId, dishId, elementId);

        return ResponseEntity.ok().body(Map.of("message", "Блюдо удалено из корзины"));
    }

}