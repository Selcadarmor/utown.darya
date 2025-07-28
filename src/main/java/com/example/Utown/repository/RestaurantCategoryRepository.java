package com.example.Utown.repository;

import com.example.Utown.model.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {
    @Query("SELECT c FROM RestaurantCategory c WHERE c.isActive = true ORDER BY c.sort")
    List<RestaurantCategory> findAllActiveRestaurantCategories();

    boolean existsByName(String name);
}
