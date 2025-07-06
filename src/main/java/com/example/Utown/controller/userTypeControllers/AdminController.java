package com.example.Utown.controller.userTypeControllers;

import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.dto.clientDTO.ClientUpdateDto;
import com.example.Utown.dto.dishDTO.DishDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.service.DishServiceImpl;
import com.example.Utown.service.UserType.client.ClientServiceImpl;
import com.example.Utown.service.RestaurantServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RequiredArgsConstructor
@Tag(
        name = "Admin – Users & Restaurants",
        description = "Admin panel for managing clients and restaurants"
)
@RestController
@RequestMapping("/admin/")
public class AdminController {

    private final ClientServiceImpl clientService;
    private final RestaurantServiceImpl restaurantService;
    private final DishServiceImpl dishService;


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
    public ResponseEntity<RestaurantDetailsDto> createRestaurant(@Valid @RequestBody RestaurantCreateDto restaurantCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createRestaurant(restaurantCreateDto));
    }

    @Operation(summary = "Update restaurant by Id", description = "Updates information of an existing restaurant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/restaurant/{id}")
    public ResponseEntity<RestaurantDetailsDto> restaurantUpdateDtoResponseEntity(@PathVariable Long id, @Valid @RequestBody RestaurantUpdateDto restaurantUpdateDto) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, restaurantUpdateDto));
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

    @Operation(
            summary = "Get all dishes by restaurant ID",
            description = "Returns a paginated list of dishes for a given restaurant ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dishes retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/restaurants/{restaurantId}/dishes")
    public ResponseEntity<Page<DishDetailsDto>> getDishesByRestaurant(
            @Parameter(description = "ID of the restaurant")
            @PathVariable Long restaurantId,

            @Parameter(description = "Page number (zero-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<DishDetailsDto> dishes = dishService.getDishesByRestaurantId(restaurantId, page, size);
        return ResponseEntity.ok(dishes);
    }


    @Operation(
            summary = "Create dish for restaurant",
            description = "Create a dish for a specific restaurant via the admin panel."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or validation failed"),
            @ApiResponse(responseCode = "404", description = "Restaurant or related resource not found (e.g., category, file)"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/restaurants/{restaurantId}/dishes")
    public ResponseEntity<DishDetailsDto> createDishForRestaurant(
            @Parameter(description = "ID of the restaurant")
            @PathVariable Long restaurantId,

            @Valid @RequestBody DishDetailsDto dishDetailsDto
    ) {
        DishDetailsDto dish = dishService.createDishForRestaurant(restaurantId, dishDetailsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dish);
    }


    @Operation(
            summary = "Update dish for restaurant by Id",
            description = "Updates an existing dish for a specific restaurant via the admin panel."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Restaurant or dish not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/restaurants/{restaurantId}/dishes/{dishId}")
    public ResponseEntity<DishDetailsDto> updateDishForRestaurant(
            @Parameter(description = "ID of the restaurant")
            @PathVariable Long restaurantId,

            @Parameter(description = "ID of the dish to update")
            @PathVariable Long dishId,

            @Valid @RequestBody DishDetailsDto dishDetailsDto
    ) {
        DishDetailsDto dish = dishService.updateDishForRestaurant(restaurantId, dishId, dishDetailsDto);
        return ResponseEntity.ok(dish);
    }



}
