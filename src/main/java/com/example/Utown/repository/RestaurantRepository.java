package com.example.Utown.repository;

import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Long countByCategory(RestaurantCategory category);
}
