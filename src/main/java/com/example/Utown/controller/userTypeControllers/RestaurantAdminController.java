package com.example.Utown.controller.userTypeControllers;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateResponseDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDto;
import com.example.Utown.dto.dishDTO.DishCreateDto;
import com.example.Utown.dto.dishDTO.DishInfoDto;
import com.example.Utown.dto.dishDTO.DishMenuDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;
import com.example.Utown.dto.orderDTO.DailyOrderStatsDto;
import com.example.Utown.dto.orderDTO.MonthlyOrderStatsDto;
import com.example.Utown.dto.orderDTO.OrderHistoryDto;
import com.example.Utown.dto.restaurantDTO.RestaurantStatusUpdateRequest;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateResponseDto;
import com.example.Utown.service.DishCategoryService;
import com.example.Utown.service.DishCategoryServiceImpl;
import com.example.Utown.service.DishService;
import com.example.Utown.service.ElementService;
import com.example.Utown.service.OperatingModeServiceImpl;
import com.example.Utown.service.OptionService;
import com.example.Utown.service.OrderService;
import com.example.Utown.service.RestaurantService;
import com.example.Utown.service.UserTypeService.RestaurantAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@PreAuthorize("hasRole('RESTAURANT_ADMIN')")
@RestController
@RequestMapping("/restaurant-admin/restaurant")
@RequiredArgsConstructor
public class RestaurantAdminController {

    private final DishService dishService;
    private final DishCategoryServiceImpl dishCategoryService; //Почему? убрать имплементацию
    private final DishCategoryService DishCategoryService;
    private final ElementService elementService;
    private final OrderService orderService;
    private final OptionService optionService;
    private final OperatingModeServiceImpl operatingModeService;
    private final RestaurantService restaurantService;
    private final RestaurantAdminService restaurantAdminService;


    @Operation(
            summary = "Get in-process orders for current restaurant",
            description = "Returns a paginated list of active orders for the authenticated restaurant admin"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved active orders"
    )
    @GetMapping("/orders/in-process")
    public Page<OrderHistoryDto> getOrdersInProcess(Pageable pageable) {
        return orderService.getOrdersInProcessByRestaurant(pageable);
    }

    @Operation(
            summary = "Get completed/canceled orders for current restaurant",
            description = "Returns a paginated list of completed, canceled, or delivered orders for the authenticated restaurant admin"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved completed/canceled orders"
    )
    @GetMapping("/orders/completed")
    public Page<OrderHistoryDto> getOrdersCompleted(Pageable pageable) {
        return orderService.getOrdersCompletedByRestaurant(pageable);
    }

    @PutMapping("order/{orderId}/accept")
    @Operation(
            summary = "Accept the order",
            description = "Changes the order status to PROCESSING and sets the cooking time."
    )
    @ApiResponse(responseCode = "200", description = "Order successfully accepted")
    @ApiResponse(responseCode = "400", description = "Invalid order status or parameters")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Void> acceptOrder(
            @PathVariable Long orderId,
            @RequestParam Integer cookingTime
    ) {
        orderService.acceptOrder(orderId, cookingTime);
        return ResponseEntity.ok().build();
    }

    @PutMapping("order/{orderId}/ready")
    @Operation(
            summary = "Mark order as ready",
            description = "Marks the order as READY_FOR_PICKUP and updates delivery status."
    )
    @ApiResponse(responseCode = "200", description = "Order marked as ready")
    @ApiResponse(responseCode = "400", description = "Order is not in PROCESSING state")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Void> readyOrder(@PathVariable Long orderId) {
        orderService.readyOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("order/{orderId}/completed")
    @Operation(
            summary = "Mark order as completed",
            description = "Marks the order as COMPLETED and sets delivery time."
    )
    @ApiResponse(responseCode = "200", description = "Order marked as completed")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Void> completedOrder(@PathVariable Long orderId) {
        orderService.completedOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("order/{orderId}/cancel")
    @Operation(
            summary = "Cancel order by admin",
            description = "Cancels the order by setting its status to CANCELED."
    )
    @ApiResponse(responseCode = "200", description = "Order successfully canceled")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Void> cancelOrderByAdmin(@PathVariable Long orderId) {
        orderService.cancelOrderByAdmin(orderId);
        return ResponseEntity.ok().build();
    }





    //UPDATE RESTAURANT INFO
    @PutMapping
    @Operation(
            summary = "Update restaurant profile by restaurant admin",
            description = "Allows a restaurant admin to update the restaurant's profile, including address, working hours, delivery zones, logo, and categories."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantUpdateResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Access denied - not the admin of this restaurant"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or data format")
    })
    public ResponseEntity<RestaurantUpdateResponseDto> updateRestaurantByAdmin(
            @RequestBody @Valid RestaurantUpdateDto dto, @PathVariable String restaurantId) {
        RestaurantUpdateResponseDto updated = restaurantService.updateRestaurantByAdmin(dto);
        return ResponseEntity.ok(updated);
    }

    // CHANGE RESTAURANT STATUS
    @Operation(
            summary = "Update restaurant status",
            description = "Allows the restaurant admin to change the  status f their restaurant"
    )
    @ApiResponse(responseCode = "200", description = "Restaurant status  successfully updated")
    @ApiResponse(responseCode = "404",description = "Restaurant not found")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "400", description = "Invalid request or status transaction not allowed")
    @PatchMapping("/status") //Passed
    public ResponseEntity<Void> updateRestaurantStatus(@RequestBody RestaurantStatusUpdateRequest request) {
        restaurantService.updateStatusForCurrentAdminRestaurant(request.getStatus());
        return  ResponseEntity.ok().build();
    }

    //OPERATING MODE
    @GetMapping("/operating-modes")
    @Operation(summary = "Get operating modes by restaurant ID",
            description = "Returns a list of operating modes for the specified restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved operating modes"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public ResponseEntity<List<OperatingModeInfoDto>> getOperatingModes() {
        Long restaurantId = restaurantAdminService.getCurrentAdmin().getRestaurant().getId();
        List<OperatingModeInfoDto> modes = operatingModeService.getOperatingModesByRestaurantId(restaurantId);
        return ResponseEntity.ok(modes);
    }

    @PutMapping("/operating-modes/{modeId}")
    @Operation(
            summary = "Update a specific operating mode",
            description = "Allows a restaurant admin to update working hours for a specific day"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operating mode updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Operating mode not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OperatingModeInfoDto> updateOperatingMode(
            @PathVariable Long modeId,
            @RequestBody OperatingModeUpdateDto dto
    ) {
        OperatingModeInfoDto result = operatingModeService.update(modeId, dto);
        return ResponseEntity.ok(result);
    }

    // DISH
    @GetMapping("/dishes/active")
    @Operation(
            summary = "Get all dishes of the current admin's restaurant",
            description = "Returns a list of dishes with their file info and allows filtering by category")
    @ApiResponses(value = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of dishes",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DishMenuDto.class)))),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Restaurant or dishes not found")
    })
    public ResponseEntity<List<DishMenuDto>> getDishesByCategory(
            @RequestParam(required = false) String categoryName) {
         List<DishMenuDto> dishes = dishService.getDishesByRestaurantWithFile(categoryName);
         return ResponseEntity.ok(dishes);
    }

    @Operation(
            summary = "Get inactive dishes by category",
            description = "Returns a list of dishes with `active = false` for the specified category in the current admin's restaurant."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inactive dishes retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DishMenuDto.class)))),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/dishes/inactive")
    public ResponseEntity<List<DishMenuDto>> getInactiveDishesByCategory(
            @RequestParam String categoryName
    ) {
        List<DishMenuDto> dishes = dishService.getInactiveDishesForRestaurant(categoryName);
        return ResponseEntity.ok(dishes);
    }

    @Operation(
            summary = "Create dish (restaurant admin)",
            description = "Creates a new dish for the restaurant assigned to the current restaurant admin")
    @ApiResponses(value = {
                    @ApiResponse(responseCode = "201", description = "Dish successfully created",
                            content = @Content(schema = @Schema(implementation = DishInfoDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - not a restaurant admin"),
                    @ApiResponse(responseCode = "404", description = "Dish category or file not found")
    })
    @PostMapping("/dishes")
    public ResponseEntity<DishInfoDto> createDishAsRestaurantAdmin(
            @RequestBody @Valid DishCreateDto dto
    ) {
        DishInfoDto created = dishService.createDishAsRestaurantAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @Operation(
            summary = "Update a dish by restaurant admin",
            description = "Updates a dish if it belongs to the restaurant of the currently authenticated restaurant admin"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied – dish does not belong to admin's restaurant"),
            @ApiResponse(responseCode = "404", description = "Dish or category or file not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PutMapping("/dishes/{dishId}")
    public ResponseEntity<DishInfoDto> updateDishByRestaurantAdmin(
            @PathVariable Long dishId,
            @RequestBody @Valid DishCreateDto dto
    ) {
        DishInfoDto updatedDish = dishService.updateDishAsRestaurantAdmin(dishId, dto);
        return ResponseEntity.ok(updatedDish);
    }

    @Operation(summary = "Delete Dish by Id", description = "Deactivates a dish in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dish deactivated successfully."),
            @ApiResponse(responseCode = "404", description = "Dish not found")
    })
    @DeleteMapping("/dishes/{dishId}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long dishId) {
        dishService.deleteDish(dishId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get deleted dishes by category",
            description = "Returns a list of dishes with `deleted = true` for the specified category in the current admin's restaurant."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted dishes retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DishMenuDto.class)))),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/dishes/deleted")
    public ResponseEntity<List<DishMenuDto>> getDeletedDishesByCategory(
            @RequestParam String categoryName
    ) {
        List<DishMenuDto> dishes = dishService.getDeletedDishesForRestaurant(categoryName);
        return ResponseEntity.ok(dishes);
    }
    //DELETE ELEMENT AND OPTION
    @Operation(summary = "Delete Option by Id", description = "Deactivates a option in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Option deactivated successfully."),
            @ApiResponse(responseCode = "404", description = "Option not found")
    })
    @DeleteMapping("/dishes/{dishId}/options/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long dishId,
                                             @PathVariable Long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete Element by Id", description = "Deactivates a element in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Element deactivated successfully."),
            @ApiResponse(responseCode = "404", description = "Element not found")
    })
    @DeleteMapping("/dishes/{dishId}/options/{optionId}/element/{elementId}")
    public ResponseEntity<Void> deleteElement(@PathVariable Long dishId,
                                              @PathVariable Long optionId,
                                              @PathVariable Long elementId) {
        elementService.deleteElement(elementId);
        return ResponseEntity.noContent().build();
    }

    //DISH CATEGORY
    @Operation(
            summary = "Get dish categories by restaurant ID",
            description = "Returns a list of all dish categories for the specified restaurant"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish categories retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DishCategoryDto.class)))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/dish-categories")
    public ResponseEntity<List<DishCategoryDto>> getCategoriesByRestaurant() {
        List<DishCategoryDto> categories = dishCategoryService.getCategoriesByRestaurant();
        return ResponseEntity.ok(categories);
    }

    @Operation(
            summary = "Create a dish category for a restaurant",
            description = "Allows a restaurant admin to create a new dish category for their restaurant"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish category created successfully",
                    content = @Content(schema = @Schema(implementation = DishCategoryCreateResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Access denied - cannot create category for another restaurant"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{restaurantId}/dish-category")
    public ResponseEntity<DishCategoryCreateResponseDto> createDishCategory(
            @PathVariable Long restaurantId,
            @RequestBody DishCategoryCreateDto dto
    ) {
        DishCategoryCreateResponseDto response =
                dishCategoryService.createDishCategoryForRestaurantByAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Update an existing dish category",
            description = "Updates the name, sort order, active status, or image of an existing dish category"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish category updated successfully",
                    content = @Content(schema = @Schema(implementation = DishCategoryDto.class))),
            @ApiResponse(responseCode = "404", description = "Dish category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/dishes/{dishCategoryId}")
    public ResponseEntity<DishCategoryDto> updateDishCategory(
            @PathVariable Long dishCategoryId,
            @RequestBody DishCategoryDto dto
    ) {
        DishCategoryDto updated = dishCategoryService.updateDishCategory(dishCategoryId, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete a dish category (soft delete)",
            description = "Marks the dish category as inactive. Throws an error if the category is linked to any dishes."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish category deleted (deactivated) successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot delete category that has associated dishes"),
            @ApiResponse(responseCode = "404", description = "Dish category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/dishes/dish-categories/{id}")
    public ResponseEntity<Void> deleteDishCategory(@PathVariable Long id) {
        dishCategoryService.deleteDishCategory(id);
        return ResponseEntity.ok().build();
    }
        //  ORDER HISTORY
    @GetMapping("/order-history/daily")
    @Operation(
            summary = "Get order statistics by day",
            description = "Return order statistics of the current restaurant for the specified month. Only for RestaurantAdmin."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received statistics by day"),
            @ApiResponse(responseCode = "401", description = "User is not authorized"),
            @ApiResponse(responseCode = "403", description = "No access to resource"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<DailyOrderStatsDto>> getDailyStats(
            @RequestParam int year,
            @RequestParam int month) {
        YearMonth ym = YearMonth.of(year, month);
        return ResponseEntity.ok(orderService.getDailyOrders(ym));
    }

    @GetMapping("/order-history/monthly")
    @Operation(
            summary = "Get monthly order statistics",
            description = "Returns order statistics for the current restaurant by month for the specified year. Accessible only by RestaurantAdmin."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monthly statistics successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Access denied – not a restaurant admin"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<MonthlyOrderStatsDto>> getMonthlyStats(
            @RequestParam int year) {

        return ResponseEntity.ok(orderService.getMonthlyStats(year));
    }

}
