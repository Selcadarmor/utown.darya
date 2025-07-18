package com.example.Utown.repository;

import com.example.Utown.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Utown.dto.dishDTO.DishDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {

    @Query("SELECT new com.example.Utown.dto.dishDTO.DishDto(d.id, d.description, d.isActive, d.isDeleted, d.price, d.sort, d.title, d.createdAt, d.updatedAt, d.restaurant.id, d.dishCategory.id, d.file.id) " +
            "FROM Dish d WHERE d.id = :id")
    Optional<DishDto> findDishById(Long id);

    @Query("SELECT new com.example.Utown.dto.dishDTO.DishDto(d.id, d.description, d.isActive, d.isDeleted, d.price, d.sort, d.title, d.createdAt, d.updatedAt, d.restaurant.id, d.dishCategory.id, d.file.id) " +
            "FROM Dish d")
    List<DishDto> findAllDishes();

    @EntityGraph(attributePaths = {"dishCategory", "file", "options.elements"})
    Page<Dish> findByRestaurantId(Long restaurantId, Pageable pageable);

    List<Dish> findAllByRestaurantId(Long restaurantId);
}


