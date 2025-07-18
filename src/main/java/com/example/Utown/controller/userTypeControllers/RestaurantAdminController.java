package com.example.Utown.controller.userTypeControllers;

import com.example.Utown.dto.orderDTO.OrderDto;
import com.example.Utown.model.User;
import com.example.Utown.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.DiscriminatorValue;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant-admin")
@RequiredArgsConstructor
public class RestaurantAdminController {

    private final OrderService orderService;

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



}
