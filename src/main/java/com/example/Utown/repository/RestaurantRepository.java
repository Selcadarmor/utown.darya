package com.example.Utown.repository;

import com.example.Utown.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT COUNT(r) FROM Restaurant r JOIN r.dishCategories c WHERE c.id = :categoryId AND r.isActive = true")
    Long countActiveRestaurantsByCategoryId(@Param("categoryId") Long categoryId);
}

