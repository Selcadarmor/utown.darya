package com.example.Utown.repository;


import com.example.Utown.model.DishCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface DishCategoryRepository extends JpaRepository<DishCategory, Long> {

    Page<DishCategory> findByRestaurantId(Long restaurantId, Pageable pageable);


}

