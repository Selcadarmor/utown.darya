package com.example.Utown.repository;

import com.example.Utown.model.Order;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.enumFiles.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRestaurant(Restaurant restaurant);

    List<Order> findByRestaurantAndStatus(Restaurant restaurant, OrderStatus status);

    Page<Order>  findAllByClientId(Long clientId, Pageable pageable);
}
