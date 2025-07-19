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
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT COUNT(r) FROM Restaurant r JOIN r.categories c WHERE c = :category")
    Long countRestaurantsByCategory(@Param("category") RestaurantCategory category);

    @Query("SELECT new com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto(" +
            "r.title, r.description, r.phone, r.minOrderAmount, COUNT(o.id), r.fileInfo.id) " +
            "FROM Restaurant r " +
            "LEFT JOIN Order o ON o.restaurant.id = r.id " +
            "WHERE r.id = :id " +
            "GROUP BY r.id, r.title, r.description, r.phone, r.minOrderAmount, r.fileInfo.id")
    Optional<RestaurantDetailsDto> findRestaurantSummaryById(@Param("id") Long id);


    @Query("""
    SELECT new com.example.Utown.dto.restaurantDTO.RestaurantInfoDto(
        r.id,
        r.title,
        a.city,
        r.phone,
        COUNT(DISTINCT o.id)
    )
    FROM Restaurant r
    LEFT JOIN r.address a
    LEFT JOIN Order o ON o.restaurant.id = r.id
    GROUP BY r.id, r.title, a.city, r.phone
""")
    Page<RestaurantInfoDto> findAllRestaurantsWithOrderCount(Pageable pageable);

    @Query("SELECT new com.example.Utown.dto.restaurantDTO.RestaurantForClientDto(" +
            "r.id, r.title, f.path, r.description, d.price, r.deliveryTime, " +
            "r.isRecommended, r.isActive, d.isDeleted) " +
            "FROM Restaurant r " +
            "JOIN r.deliveries d " +
            "LEFT JOIN r.fileInfo f " +
            "WHERE r.isActive = true " +
            "  AND d.isActive = true " +
            "  AND d.isDeleted = false " +
            "  AND r.address.state = :state " +
            "  AND d.district = :city " +
            "  AND (d.area IS NULL OR d.area = :area) " +
            "ORDER BY r.isRecommended DESC"
    )
    Page<RestaurantForClientDto> findRecommendedRestaurants(
            @Param("state") String state,
            @Param("city") String city,
            @Param("area") String area,
            Pageable pageable
    );

    @Query("SELECT new com.example.Utown.dto.restaurantDTO.RestaurantForClientDto(" +
            "r.id, r.title, f.path, r.description, d.price, r.deliveryTime, " +
            "r.isRecommended, r.isActive, d.isDeleted) " +
            "FROM Restaurant r " +
            "JOIN r.deliveries d " +
            "LEFT JOIN r.fileInfo f " +
            "WHERE r.isActive = true " +
            "  AND d.isActive = true " +
            "  AND d.isDeleted = false " +
            "  AND r.address.state = :state " +
            "  AND d.district = :city " +
            "  AND (d.area IS NULL OR d.area = :area) " +
            "ORDER BY d.price ASC, r.deliveryTime ASC"
    )
    Page<RestaurantForClientDto> findFastestDeliveryRestaurants(
            @Param("state") String state,
            @Param("city") String city,
            @Param("area") String area,
            Pageable pageable
    );

    @Query("SELECT new com.example.Utown.dto.restaurantDTO.RestaurantForClientDto(" +
            "r.id, r.title, f.path, r.description, d.price, r.deliveryTime, " +
            "r.isRecommended, r.isActive, d.isDeleted) " +
            "FROM Restaurant r " +
            "JOIN r.categories c " +
            "JOIN r.deliveries d " +
            "LEFT JOIN r.fileInfo f " +
            "WHERE r.isActive = true " +
            "  AND d.isActive = true " +
            "  AND d.isDeleted = false " +
            "  AND d.district = :city " +
            "  AND (d.area IS NULL OR d.area = :area) " +
            "  AND c.id = :categoryId"
    )
    Page<RestaurantForClientDto> findRestaurantsByCategory(
            @Param("state") String state,
            @Param("city") String city,
            @Param("area") String area,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    @Query("SELECT DISTINCT new com.example.Utown.dto.restaurantDTO.RestaurantForClientDto(" +
            "r.id, r.title, f.path, r.description, d.price, r.deliveryTime, " +
            "r.isRecommended, r.isActive, d.isDeleted) " +
            "FROM Restaurant r " +
            "JOIN r.deliveries d " +
            "LEFT JOIN r.categories c " +
            "LEFT JOIN r.fileInfo f " +
            "WHERE d.isActive = true " +
            "AND d.isDeleted = false " +
            "AND d.district = :city " +
            "AND (:area IS NULL OR d.area = :area) " +
            "AND (LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')))"
    )
    Page<RestaurantForClientDto> searchClient(
            @Param("query") String query,
            @Param("state") String state,
            @Param("city") String city,
            @Param("area") String area,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE Restaurant r SET r.isActive = false WHERE r.id = :id")
    void deactivateById(@Param("id") Long id);

}
