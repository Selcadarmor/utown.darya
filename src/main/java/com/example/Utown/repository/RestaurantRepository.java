package com.example.Utown.repository;

import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT COUNT(r) FROM Restaurant r JOIN r.categories c WHERE c = :category")
    Long countRestaurantsByCategory(@Param("restaurant_category") RestaurantCategory category);


    @Query("SELECT r FROM Restaurant r " +       // метод получения ресторана по айди
            "LEFT JOIN FETCH r.categories " +
            "LEFT JOIN FETCH r.operatingModes " +
            "LEFT JOIN FETCH r.deliveries " +
            "LEFT JOIN FETCH r.fileInfo " +
            "LEFT JOIN FETCH r.restaurantAdmin " +
            "WHERE  r.id = :id")
    Optional<Restaurant> findRestaurantById(@Param ("id") Long id);

    @Query("SELECT new com.example.Utown.dto.restaurantDTO.RestaurantInfoDto(" +
            "r.id, r.title, a.city, r.phone, COUNT(o.id), r.createdAt, r.updatedAt) " +
            "FROM Restaurant r " +
            "LEFT JOIN Order o ON o.restaurant.id = r.id " +
            "LEFT JOIN Client c ON o.client.id = c.id " +
            "LEFT JOIN c.addresses a " +
            "GROUP BY r.id, r.title, a.city, r.phone, r.createdAt, r.updatedAt")
    Page<RestaurantInfoDto> findAllRestaurantsWithOrderCount(Pageable pageable);


    @Query("""
        SELECT DISTINCT r
        FROM Restaurant r
        LEFT JOIN FETCH r.fileInfo
        LEFT JOIN FETCH r.deliveries
        LEFT JOIN FETCH r.categories
        WHERE r.isActive = true
    """)
    List<Restaurant> getAllActiveRestaurantsForClient();

    @Query("""
        SELECT DISTINCT r
        FROM Restaurant r
        LEFT JOIN r.categories c
        WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    Page<Restaurant> searchClient(@Param("query") String query, Pageable pageable);


}
