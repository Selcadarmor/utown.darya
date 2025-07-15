package com.example.Utown.repository;

import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT COUNT(r) FROM Restaurant r JOIN r.categories c WHERE c = :category")
    Long countRestaurantsByCategory(@Param("category") RestaurantCategory category);

    @Query("SELECT new com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto(" +
            "r.id, r.title, r.description, r.phone, r.minOrderAmount, COUNT(o.id), r.fileInfo.id) " +
            "FROM Restaurant r " +
            "LEFT JOIN Order o ON o.restaurant.id = r.id " +
            "WHERE r.id = :id " +
            "GROUP BY r.id, r.title, r.description, r.phone, r.minOrderAmount, r.fileInfo.id")
    Optional<RestaurantDetailsDto> findRestaurantDetailsById(@Param("id") Long id);


    @Query("SELECT new com.example.Utown.dto.restaurantDTO.RestaurantInfoDto(" +
            "r.id, r.title, a.city, r.phone, COUNT(o.id), r.createdAt, r.updatedAt) " +
            "FROM Restaurant r " +
            "LEFT JOIN Order o ON o.restaurant.id = r.id " +
            "LEFT JOIN Client c ON o.client.id = c.id " +
            "LEFT JOIN c.addresses a " +
            "GROUP BY r.id, r.title, a.city, r.phone, r.createdAt, r.updatedAt")
    Page<RestaurantInfoDto> findAllRestaurantsWithOrderCount(Pageable pageable);


    @Query("""
    SELECT new com.example.Utown.dto.restaurantDTO.RestaurantForClientDto(
        r.id,
        r.title,
        f.path,
        d.price,
        r.deliveryTime,
        r.isRecommended,
        r.isActive
    )
    FROM Restaurant r
    JOIN r.deliveries d
    LEFT JOIN r.fileInfo f
    WHERE d.area = :area
      AND d.isActive = true
      AND r.isActive = true
""")
    List<RestaurantForClientDto> getRestaurantsByCityAndArea(
            @Param("area") String area
    );




    @Query("""
        SELECT DISTINCT r
        FROM Restaurant r
        LEFT JOIN r.categories c
        WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    Page<Restaurant> searchClient(@Param("query") String query, Pageable pageable);

    @Modifying
    @Query("UPDATE Restaurant r SET r.isActive = false WHERE r.id = :id")
    void deactivateById(@Param("id") Long id);

}
