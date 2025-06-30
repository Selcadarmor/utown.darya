package com.example.Utown.repository;

import com.example.Utown.model.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {
    Optional<RestaurantCategory> findByName(String name);
}
