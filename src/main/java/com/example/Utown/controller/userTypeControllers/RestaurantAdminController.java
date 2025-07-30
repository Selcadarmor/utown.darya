package com.example.Utown.controller.userTypeControllers;

import com.example.Utown.dto.orderDTO.OrderDto;
import com.example.Utown.dto.restaurantDTO.RestaurantStatusUpdateRequest;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.service.DishService;
import com.example.Utown.service.ElementService;
import com.example.Utown.service.OptionService;
import com.example.Utown.model.UserType.User;
import com.example.Utown.service.OrderService;
import com.example.Utown.service.RestaurantService;
import com.example.Utown.service.UserTypeService.RestaurantAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurant-admin")
@RequiredArgsConstructor
public class RestaurantAdminController {

    private final OrderService orderService;
    private final RestaurantService restaurantService;
    private final RestaurantAdminService restaurantAdminService;
    private final DishService dishService;
    private final OptionService optionService;
    private final ElementService elementService;

    @PatchMapping("/orders/{orderId}/accept")
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    @Operation(summary = "Принять заказ", description = "Ресторанный админ подтверждает заказ")
    public ResponseEntity<?> acceptOrder(@PathVariable Long orderId) {
        orderService.acceptOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/orders/{orderId}/reject")
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    @Operation(summary = "Отклонить заказ", description = "Ресторанный админ отклоняет заказ")
    public ResponseEntity<?> rejectOrder(@PathVariable Long orderId) {
        orderService.rejectOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/orders/{orderId}/cancel")
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    @Operation(summary = "Отменить заказ", description = "Ресторанный админ отменяет заказ")
    public ResponseEntity<?> cancelOrderByAdmin(@PathVariable Long orderId) {
        orderService.cancelOrderByAdmin(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    @Operation(summary = "Получить все заказы", description = "Ресторанный админ получает список всех заказов своего ресторана")
    public ResponseEntity<List<OrderDto>> getAllOrdersForRestaurantAdmin(
            @AuthenticationPrincipal User user
    ) {
        List<OrderDto> orders = orderService.getAllOrdersForRestaurantAdmin(user.getUsername());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/status/{status}")
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    @Operation(summary = "Фильтрация заказов по статусу", description = "Ресторанный админ фильтрует заказы по статусу (PENDING, CANCELED и т.д.)")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(
            @PathVariable String status,
            @AuthenticationPrincipal User user
    ) {
        List<OrderDto> orders = orderService.getOrdersByStatusForRestaurantAdmin(user.getUsername(), status);
        return ResponseEntity.ok(orders);
    }

    @Operation(
            summary = "Update restaurant status",
            description = "Allows the restaurant admin to chenge the  status f their restaurant"
    )
    @ApiResponse(responseCode = "200", description = "Restaurant status  siccessfully updated")
    @ApiResponse(responseCode = "404",description = "Restaurant not found")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "400", description = "Invalodreques or statustransaction not allowed")
    @PatchMapping("/restaurants/status")
    public ResponseEntity<Void> updateRestaurantStatus(@RequestBody RestaurantStatusUpdateRequest request) {
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        restaurantService.updateStatusForRestaurantAdmin(request.getStatus(), currentAdmin.getId());
        return  ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete Dish by Id", description = "Deactivates a dish in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dish deactivated successfully."),
            @ApiResponse(responseCode = "404", description = "Dish not found")
    })
    @DeleteMapping("/restaurants/dishes/{dishId}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long dishId) {
        dishService.deleteDish(dishId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete Option by Id", description = "Deactivates a option in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Option deactivated successfully."),
            @ApiResponse(responseCode = "404", description = "Option not found")
    })
    @DeleteMapping("/restaurant/dishes/{dishId}/options/{optionId}")
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
    @DeleteMapping("/restaurant/dishes/{dishId}/option/{optionId}/element/{elementId}")
    public ResponseEntity<Void> deleteElement(@PathVariable Long dishId,
                                              @PathVariable Long optionId,
                                              @PathVariable Long elementId) {
        elementService.deleteElement(elementId);
        return ResponseEntity.noContent().build();
    }
}
