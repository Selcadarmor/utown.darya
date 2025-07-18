package com.example.Utown.repository;

import com.example.Utown.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Long countByRestaurantId(Long restaurantId);



}
