package com.example.Utown.repository;

import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Long countByCategory(RestaurantCategory category);

    @Query(""" 
        SELECT r FROM Restaurant r
        LEFT JOIN FETCH r.address
        LEFT JOIN FETCH r.category
        LEFT JOIN FETCH r.fileInfo
        LEFT JOIN FETCH r.orders
        LEFT JOIN FETCH r.operatingModes
    """)
    List<Restaurant> findAllRestaurants();

    @Query(""" 
        SELECT r FROM Restaurant r
        LEFT JOIN FETCH r.address
        LEFT JOIN FETCH r.category
        LEFT JOIN FETCH r.fileInfo
        LEFT JOIN FETCH r.orders
        LEFT JOIN FETCH r.operatingModes
    """)
    Optional<Restaurant> findRestaurantById(Long id);


}
