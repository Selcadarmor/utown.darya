package com.example.Utown.controller;

import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.dto.clientDTO.ClientUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantCreateUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.service.UserType.client.ClientServiceImpl;
import com.example.Utown.service.RestaurantServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RequiredArgsConstructor
@Tag(
        name = "Admin – Users & Restaurants",
        description = "Admin panel for managing clients, restaurants, and restaurant administrators"
)
@RestController
@RequestMapping("/admin/")
public class AdminClientController {

    private final ClientServiceImpl clientService;
    private final RestaurantServiceImpl restaurantService;

    @Operation(summary = "Get all clients", description = "Returns a list of all registered clients.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of clients retrieved successfully")
    })
    @GetMapping("/clients")
    public ResponseEntity<List<ClientInfoDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @Operation(summary = "Get client by id", description = "Returns a client with the given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/client/{id}")
    public ResponseEntity<ClientInfoDto> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }


    @Operation(summary = "Update client by Id", description = "Updates information of an existing client.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("//client{id}")
    public ResponseEntity<ClientInfoDto> clientUpdateDtoResponseEntity(@PathVariable Long id, @Valid @RequestBody ClientUpdateDto clientUpdateDto) {
        return ResponseEntity.ok(clientService.updateClient(id, clientUpdateDto));
    }

    @Operation(summary = "Delete client by Id", description = "Removes a client from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/client{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary =  "Get all restaurants", description = "Returns a list of all restaurants.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of restaurants retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantInfoDto>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @Operation(summary = "Get restaurant by id", description = "Returns a restaurant with the given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/restaurant/{id}")
    public ResponseEntity<RestaurantDetailsDto> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @Operation(summary = "Create restaurant", description = "Create Restaurant via the admin panel.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or validation failed"),
            @ApiResponse(responseCode = "404", description = "Related resource not found (e.g., category)"),
            @ApiResponse(responseCode = "409", description = "Restaurant with such data already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/restaurant/")
    public ResponseEntity<RestaurantDetailsDto> createRestaurant(@Valid @RequestBody RestaurantCreateUpdateDto restaurantCreateUpdateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createRestaurant(restaurantCreateUpdateDto));
    }

    @Operation(summary = "Update restaurant by Id", description = "Updates information of an existing restaurant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/restaurant/{id}")
    public ResponseEntity<RestaurantDetailsDto> restaurantUpdateDtoResponseEntity(@PathVariable Long id, @Valid @RequestBody RestaurantCreateUpdateDto restaurantCreateUpdateDto) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, restaurantCreateUpdateDto));
    }

    @Operation(summary = "Delete restaurant by Id", description = "Removes a restaurant from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurant deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/restaurant/{id}")
    public void deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
    }
}
