package com.example.Utown.repository;


import com.example.Utown.model.DishCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface DishCategoryRepository extends JpaRepository<DishCategory, Long> {
    Page<DishCategory> findByRestaurantId(Long restaurantId, Pageable pageable);

    @Query("SELECT COUNT(d) FROM Dish d WHERE d.dishCategory.id = :categoryId")
    Long countDishesByDishCategoryId(@Param("categoryId") Long categoryId);


}

