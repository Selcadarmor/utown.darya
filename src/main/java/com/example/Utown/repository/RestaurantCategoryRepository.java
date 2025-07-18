package com.example.Utown.repository;

import com.example.Utown.model.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {
    Optional<RestaurantCategory> findByName(String name);

    @Query("SELECT rc FROM RestaurantCategory rc WHERE rc.isActive = true ORDER BY rc.sort ASC")
    List<RestaurantCategory> findAllActiveRestaurantCategories();


}
