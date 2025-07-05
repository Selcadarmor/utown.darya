package com.example.Utown.repository;

import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT COUNT(r) FROM Restaurant r JOIN r.categories c WHERE c = :category")
    Long countRestaurantsByCategory(@Param("restaurant_category") RestaurantCategory category);


    @Query(""" 
        SELECT r FROM Restaurant r
        LEFT JOIN FETCH r.address
        LEFT JOIN FETCH r.categories
        LEFT JOIN FETCH r.fileInfo
        LEFT JOIN FETCH r.operatingModes
    """)
    List<Restaurant> findAllRestaurants();

    @Query(""" 
        SELECT r FROM Restaurant r
        LEFT JOIN FETCH r.address
        LEFT JOIN FETCH r.categories
        LEFT JOIN FETCH r.fileInfo
        LEFT JOIN FETCH r.operatingModes
    """)
    Optional<Restaurant> findRestaurantById(Long id);

    @Query("""
    SELECT new com.example.Utown.dto.restaurantDTO.RestaurantInfoDto(
        r.id, r.title, COUNT(o.id)
    )
    FROM Restaurant r
    LEFT JOIN Order o ON o.restaurant.id = r.id
    GROUP BY r.id, r.title
""")
    List<RestaurantInfoDto> findAllRestaurantsWithOrderCount();

    @Query("""
        SELECT DISTINCT r
        FROM Restaurant r
        LEFT JOIN FETCH r.fileInfo
        LEFT JOIN FETCH r.delivery
        LEFT JOIN FETCH r.categories
        WHERE r.isActive = true
    """)
    List<Restaurant> getAllActiveRestaurantsForClient();



}
