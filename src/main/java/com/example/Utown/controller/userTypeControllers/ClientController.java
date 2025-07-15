package com.example.Utown.controller.userTypeControllers;


import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryRestaurantProfileDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantDTO.RestaurantProfileDto;
import com.example.Utown.service.*;
import com.example.Utown.service.UserType.client.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/restaurant_categories") //Passed
    @Operation(summary = "Get all restaurant categories", description = "Restaurant categories with restaurants count for client")
    public ResponseEntity<List<RestaurantCategoryForClient>> getAllCategoriesForClient() {
        List<RestaurantCategoryForClient> categories =
                restaurantCategoryService.getAllCategoriesWithRestaurantCount();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/restaurants")
    @Operation(summary = "Get all restaurants", description = "Sorted by recommendation")
    public ResponseEntity<Page<RestaurantForClientDto>> getRecommendedRestaurantsForClient(Pageable pageable) {
        Page<RestaurantForClientDto> restaurants = restaurantService.getRecommendedRestaurantsForClient(pageable);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/restaurants_fastest_delivery")
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


}