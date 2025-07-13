package com.example.Utown.repository;

import com.example.Utown.model.Order;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.enumFiles.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Long countByRestaurantId(Long restaurantId);
    List<Order> findByRestaurant(Restaurant restaurant);

    List<Order> findByRestaurantAndStatus(Restaurant restaurant, OrderStatus status);
}
