package com.example.Utown.controller.userTypeControllers;


import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.clientDTO.ClientRegistrationDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.service.AddressService;
import com.example.Utown.service.AuthService;
import com.example.Utown.service.RestaurantCategoryService;
import com.example.Utown.service.UserType.client.ClientService;
import com.example.Utown.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final AuthService authService;

    @GetMapping("/restaurant_categories")
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


}