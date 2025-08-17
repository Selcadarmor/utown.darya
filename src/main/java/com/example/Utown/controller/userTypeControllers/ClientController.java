package com.example.Utown.controller.userTypeControllers;


import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryRestaurantProfileDto;
import com.example.Utown.dto.dishDTO.DishForClientDto;
import com.example.Utown.dto.dishDTO.DishSearchDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderRequestDto;
import com.example.Utown.dto.orderDTO.OrderHistoryDto;
import com.example.Utown.dto.ratingDTO.RatingDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantDTO.RestaurantProfileDto;
import com.example.Utown.service.AddressService;
import com.example.Utown.service.CartService;
import com.example.Utown.service.DishCategoryService;
import com.example.Utown.service.DishService;
import com.example.Utown.service.OrderService;
import com.example.Utown.service.RatingService;
import com.example.Utown.service.RestaurantCategoryService;
import com.example.Utown.service.RestaurantService;
import com.example.Utown.service.UserTypeService.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Tag(name = "Client", description = "Client specific operations")
public class ClientController {

    private final AddressService addressService;
    private final CartService cartService;
    private final ClientService clientService;
    private final DishCategoryService dishCategoryService;
    private final DishService dishService;
    private final OrderService orderService;
    private final RatingService ratingService;
    private final RestaurantCategoryService restaurantCategoryService;
    private final RestaurantService restaurantService;

    @GetMapping("/restaurant/categories") //Passed
    @Operation(
            summary = "Get all restaurant categories",
            description = "Restaurant categories with restaurants count for client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved restaurant categories"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<RestaurantCategoryForClient>> getAllCategoriesForClient() {
        List<RestaurantCategoryForClient> categories = restaurantCategoryService.getAllCategoriesWithRestaurantCount();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/restaurants") //Passed
    @Operation(
            summary = "Get all restaurants",
            description = "Sorted by recommendation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved restaurants"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<Page<RestaurantForClientDto>> getRecommendedRestaurantsForClient(Pageable pageable) {
        Page<RestaurantForClientDto> restaurants = restaurantService.getRecommendedRestaurantsForClient(pageable);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/restaurants/fastest_delivery") //Passed
    @Operation(
            summary = "Get all restaurants",
            description = "Sorted by DeliveryTime",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved fastest delivery restaurants"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<Page<RestaurantForClientDto>> getFastestDeliveryRestaurantsForClient(Pageable pageable) {
        Page<RestaurantForClientDto> restaurants = restaurantService.getFastestDeliveryRestaurantsForClient(pageable);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/restaurants/categories/{categoryId}") //Passed
    @Operation(
            summary = "Get all restaurants by Restaurant Category",
            description = "Get all restaurants by Restaurant Category",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved restaurants by category"),
                    @ApiResponse(responseCode = "404", description = "Category not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<Page<RestaurantForClientDto>> getRestaurantsByCategory(@PathVariable Long categoryId, Pageable pageable) {
        Page<RestaurantForClientDto> restaurants = restaurantService.getRestaurantsByCategory(categoryId, pageable);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/favourites") //Passed
    @Operation(summary = "Get favorite restaurants", description = "Returns the list of restaurants added to client's favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of favorite restaurants returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<RestaurantForClientDto>> getFavoriteRestaurants() {
        List<RestaurantForClientDto> favorites = clientService.getFavoriteRestaurants();
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/restaurants/search") //Passed
    @Operation(
            summary = "Search restaurants",
            description = "Search restaurants by query with sorting and pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved restaurants by search query"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<Page<RestaurantForClientDto>> searchRestaurants(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Page<RestaurantForClientDto> restaurants = restaurantService.searchRestaurants(query, page, size, sortBy, direction);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("restaurant/profile/{restaurantId}") //Passed
    @Operation(
            summary = "Get restaurant profile",
            description = "Returns profile info for restaurant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved restaurant profile"),
                    @ApiResponse(responseCode = "404", description = "Restaurant not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<RestaurantProfileDto> getRestaurantProfile(@PathVariable Long restaurantId) {
        RestaurantProfileDto profile = restaurantService.getRestaurantProfile(restaurantId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/restaurant/profile/{restaurantId}/search/dishes") //Passed
    @Operation(
            summary = "Search dishes by keyword in a specific restaurant",
            description = "Returns a paginated list of dishes filtered by keyword and restaurant ID. Search is case-insensitive and matches title or description."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dishes found and returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<DishSearchDto>> searchDishes(
            @RequestParam Long restaurantId,
            @RequestParam String keyword,
            @PageableDefault(size = 10, sort = "sort", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<DishSearchDto> dishes = dishService.searchDishesByRestaurant(restaurantId, keyword, pageable);
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("restaurant/profile/{restaurantId}/dish_categories") //Passed
    @Operation(
            summary = "Get dish categories for restaurant",
            description = "Returns all dish categories for a specific restaurant with dish count",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved dish categories"),
                    @ApiResponse(responseCode = "404", description = "Restaurant not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<DishCategoryRestaurantProfileDto>> getDishCategoriesByRestaurant(@PathVariable Long restaurantId) {
        List<DishCategoryRestaurantProfileDto> categories = dishCategoryService.getDishCategoriesByRestaurantForClient(restaurantId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("dish/category/{categoryId}") //Passed
    @Operation(summary = "Get dishes by category", description = "Returns all dishes for a given category, each with options and elements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of dishes returned successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found or no dishes"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<DishSearchDto>> getDishesByCategory(@PathVariable Long categoryId) {
        List<DishSearchDto> dishes = dishService.getDishesByCategory(categoryId);
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("dish/{dishId}") //Passed
    @Operation(summary = "Get dish by ID", description = "Returns a single dish with options and elements by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish returned successfully"),
            @ApiResponse(responseCode = "404", description = "Dish not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<DishForClientDto> getDishById(@PathVariable Long dishId) {
        DishForClientDto dish = dishService.getDishByIdForOrder(dishId);
        return ResponseEntity.ok(dish);
    }

    @GetMapping("/addresses") //Passed
    @Operation(
            summary = "Get all addresses for current client",
            description = "Returns addresses for authenticated client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved addresses"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<AddressDto>> getClientAddresses() {
        List<AddressDto> addresses = clientService.getAddressesByClient();
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/cart")
    @Operation(summary = "Get current client's cart details",
            description = "Returns the cart with dishes, delivery price, and total sum")
    @ApiResponse(responseCode = "200", description = "Cart returned successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<CartDto> getCart() {
        CartDto cartDto = cartService.getCart();
        return ResponseEntity.ok(cartDto);
    }

    @Operation(summary = "Get client's order history with pagination")
    @ApiResponse(responseCode = "200", description = "Order history retrieved successfully")
    @GetMapping("order/history")
    public ResponseEntity<Page<OrderHistoryDto>> getOrderHistory(@PageableDefault(size = 10) Pageable pageable) {
        Page<OrderHistoryDto> history = orderService.getOrderHistoryByClient(pageable);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/address/create") //Passed
    @Operation(
            summary = "Add address for current user",
            description = "Add address to currently authenticated user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Address created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<Void> addAddressForCurrentUser(@RequestBody AddressDto addressDto) {
        clientService.saveAddressForClient(addressDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/dish/{dishId}/cart/add/")
    @Operation(
            summary = "Add dish to cart",
            description = "Adds a selected dish with options to the current client's cart"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dish successfully added to cart"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Dish or elements not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> addDishToCart(
            @Parameter(description = "ID of the dish to add", required = true)
            @PathVariable Long dishId,
            @Parameter(description = "Details of the dish order", required = true)
            @RequestBody DishToOrderRequestDto dto) {

        cartService.addDishToCart(dishId, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/order")
    @Operation(
            summary = "Create order from current client's cart",
            description = "Creates an order from the client's current cart, moving all DishToOrder entries into the order without deletion."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Cart is empty or invalid"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
    })
    public ResponseEntity<Void> createOrderFromCart() {
        orderService.createOrderFromCart();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("order/{orderId}/cancel")
    @Operation(summary = "Cancel order by client", description = "Allows the current client to cancel their order if allowed by status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order canceled successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied to cancel this order"),
            @ApiResponse(responseCode = "400", description = "Order cancellation is not allowed at this stage"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<String> cancelOrderByClient(@PathVariable Long orderId) {
        orderService.cancelOrderByClient(orderId);
        return ResponseEntity.ok("Order canceled successfully");
    }

    @PostMapping("/dish/{dishId}/rating")
    @Operation(summary = "Create rating for a dish")
    @ApiResponse(responseCode = "200", description = "Rating created successfully")
    @ApiResponse(responseCode = "404", description = "Dish not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Void> createRatingByDish(
            @PathVariable Long dishId,
            @RequestBody RatingDto ratingDto
    ) {
        ratingService.createRatingByDish(dishId, ratingDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/restaurant/{restaurantId}/rating")
    @Operation(summary = "Create rating for a restaurant")
    @ApiResponse(responseCode = "200", description = "Rating created successfully")
    @ApiResponse(responseCode = "404", description = "Restaurant not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Void> createRatingByRestaurant(
            @PathVariable Long restaurantId,
            @RequestBody RatingDto ratingDto
    ) {
        ratingService.createRatingByRestaurant(restaurantId, ratingDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/profile/update") // Passed
    @Operation(
            summary = "Update client profile",
            description = "Update full name and default address for the current client. " ,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Default address not set or invalid data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Default address not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ClientProfileUpdateDto> updateClientProfile(@RequestBody ClientProfileUpdateDto dto) {
        ClientProfileUpdateDto updatedProfile = clientService.updateClientProfile(dto);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping("/addresses/update/{id}") //Passed
    @Operation(
            summary = "Update address by ID",
            description = "Update address details by address ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Address updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Address not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<Void> updateAddress(
            @PathVariable Long id,
            @RequestBody AddressDto addressDto
    ) {
        addressService.updateAddress(id, addressDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/dish-to-order/{dishToOrderId}") //Passed
    @Operation(
            summary = "Update dish position in the cart",
            description = "Updates the selected dish in the cart and returns the updated cart"
    )
    @ApiResponse(responseCode = "200", description = "Cart updated successfully")
    public ResponseEntity<CartDto> updateDishInCart(
            @PathVariable Long dishToOrderId,
            @RequestBody DishToOrderRequestDto dto) {
        CartDto updatedCart = cartService.updateCart(dishToOrderId, dto);
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/favorites/{restaurantId}") //Passed
    @Operation(
            summary = "Add restaurant to favorites",
            description = "Add a restaurant to the client's favorite list",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Restaurant added to favorites"),
                    @ApiResponse(responseCode = "404", description = "Restaurant not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized client")
            }
    )
    public ResponseEntity<Void> addFavoriteRestaurant(@PathVariable Long restaurantId) {
        clientService.addFavoriteRestaurant(restaurantId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/addresses/delete/{id}") //Passed
    @Operation(
            summary = "Delete address for current client",
            description = "Deletes an address belonging to the authenticated client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Address deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Address not found"),
            })
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/dish-to-order/{dishToOrderId}/delete") //Passed
    @Operation(
            summary = "Remove dish from cart",
            description = "Deletes the specified dish from the cart and returns the updated cart"
    )
    @ApiResponse(responseCode = "200", description = "Dish removed and cart updated successfully")
    public ResponseEntity<CartDto> removeDishFromCart(@PathVariable Long dishToOrderId) {
        CartDto updatedCart = cartService.removeDishFromCart(dishToOrderId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{cartId}/clear") //Passed
    @Operation(
            summary = "Clear cart by ID",
            description = "Removes all dishes from the specified cart and recalculates totals"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CartDto> clearCartById(@PathVariable Long cartId) {
        CartDto clearedCart = cartService.clearCart(cartId);
        return ResponseEntity.ok(clearedCart);
    }

    @DeleteMapping("/favorites/{restaurantId}") //Passed
    @Operation(summary = "Remove restaurant from favorites", description = "Removes a restaurant from the client's favorite list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant removed from favorites"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> removeFavoriteRestaurant(@PathVariable Long restaurantId) {
        clientService.removeFavoriteRestaurant(restaurantId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Delete a rating by ID",
            description = "Allows an authenticated client to deleteOption a specific rating by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Rating not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping("rating/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long ratingId) {
        ratingService.deleteRating(ratingId);
        return ResponseEntity.ok().build();
    }



}