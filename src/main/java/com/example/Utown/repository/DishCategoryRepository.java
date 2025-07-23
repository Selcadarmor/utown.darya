package com.example.Utown.repository;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryRestaurantProfileDto;
import com.example.Utown.model.DishCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishCategoryRepository extends JpaRepository<DishCategory, Long> {
    Page<DishCategory> findByRestaurantId(Long restaurantId, Pageable pageable);

    @Query(
            "SELECT new com.example.Utown.dto.dishCategoryDTO.DishCategoryRestaurantProfileDto(" +
                    "dc.id, dc.name, dc.sort, dc.isActive, f.path, " +
                    "COUNT(d.id)) " +
                    "FROM DishCategory dc " +
                    "LEFT JOIN dc.dishes d " +
                    "LEFT JOIN dc.file f " +
                    "WHERE dc.restaurant.id = :restaurantId AND dc.isActive = true " +
                    "GROUP BY dc.id, dc.name, dc.sort, dc.isActive, f.path"
    )
    List<DishCategoryRestaurantProfileDto> findDishCategoriesWithDishCountByRestaurantId(@Param("restaurantId") Long restaurantId);




}

