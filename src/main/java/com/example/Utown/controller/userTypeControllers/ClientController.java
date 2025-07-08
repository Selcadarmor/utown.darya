package com.example.Utown.controller.userTypeControllers;


import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientChangePasswordDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.service.AddressService;
import com.example.Utown.service.RestaurantCategoryService;
import com.example.Utown.service.UserType.client.ClientService;
import com.example.Utown.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{clientId}/addresses")
    @Operation(summary = "Get addresses for client", description = "Get addresses by client ID")
    public ResponseEntity<Void> addAddressForClient(
            @PathVariable Long clientId,
            @RequestBody AddressDto addressDto) {

        clientService.saveAddressForClient(clientId, addressDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/update")
    @Operation(summary = "Update", description = "Update fullname and addresses for client")
    public ResponseEntity<Void> updateClientProfile(
            @PathVariable Long id,
            @RequestBody ClientProfileUpdateDto dto
    ) {
        clientService.updateClientProfile(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get Addresses for client", description = "")
    public ResponseEntity<List<AddressDto>> getClientAddresses(@PathVariable Long clientId) {
        List<AddressDto> addresses = addressService.getAddressesByClientId(clientId);
        return ResponseEntity.ok(addresses);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Get Addresses for client", description = "")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
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

}