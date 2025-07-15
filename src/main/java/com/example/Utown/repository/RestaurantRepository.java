package com.example.Utown.repository;

import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT COUNT(r) FROM Restaurant r JOIN r.categories c WHERE c = :category")
    Long countRestaurantsByCategory(@Param("category") RestaurantCategory category);

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

    @Query("SELECT new com.example.Utown.dto.restaurantDTO.RestaurantForClientDto(" +
            "r.id, " +
            "r.title, " +
            "f.path, " +
            "d.price, " +
            "r.deliveryTime, " +
            "r.isRecommended, " +
            "r.isActive, " +
            "d.isDeleted" +
            ") " +
            "FROM Restaurant r " +
            "JOIN r.deliveries d " +
            "LEFT JOIN r.fileInfo f " +
            "WHERE r.isActive = true " +
            "  AND d.isActive = true " +
            "  AND d.isDeleted = false " +
            "  AND d.district = :city " +
            "  AND (d.area IS NULL OR d.area = :area) " +
            "ORDER BY r.isRecommended DESC"
    )
    Page<RestaurantForClientDto> findRecommendedRestaurants(
            @Param("city") String city,
            @Param("area") String area,
            Pageable pageable
    );

    @Query("SELECT new com.example.Utown.dto.restaurantDTO.RestaurantForClientDto(" +
            "r.id, " +
            "r.title, " +
            "f.path, " +
            "d.price, " +
            "r.deliveryTime, " +
            "r.isRecommended, " +
            "r.isActive, " +
            "d.isDeleted" +
            ") " +
            "FROM Restaurant r " +
            "JOIN r.deliveries d " +
            "LEFT JOIN r.fileInfo f " +
            "WHERE r.isActive = true " +
            "  AND d.isActive = true " +
            "  AND d.isDeleted = false " +
            "  AND d.district = :city " +
            "  AND (d.area IS NULL OR d.area = :area) " +
            "ORDER BY d.price ASC, r.deliveryTime ASC"
    )
    Page<RestaurantForClientDto> findFastestDeliveryRestaurants(
            @Param("city") String city,
            @Param("area") String area,
            Pageable pageable
    );

    @Query("SELECT new com.example.Utown.dto.restaurantDTO.RestaurantForClientDto(" +
            "r.id, " +
            "r.title, " +
            "f.path, " +
            "d.price, " +
            "r.deliveryTime, " +
            "r.isRecommended, " +
            "r.isActive, " +
            "d.isDeleted" +
            ") " +
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
            @Param("city") String city,
            @Param("area") String area,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    @Query("SELECT DISTINCT new com.example.Utown.dto.restaurantDTO.RestaurantForClientDto(" +
            "r.id, " +
            "r.title, " +
            "f.path, " +
            "d.price, " +
            "r.deliveryTime, " +
            "r.isRecommended, " +
            "r.isActive, " +
            "d.isDeleted " +
            ") " +
            "FROM Restaurant r " +
            "JOIN r.deliveries d " +
            "LEFT JOIN r.categories c " +
            "LEFT JOIN r.fileInfo f " +
            "WHERE d.isActive = true " +
            "AND d.isDeleted = false " +
            "AND d.district = :city " +
            "AND (LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')))"
    )
    Page<RestaurantForClientDto> searchClient(
            @Param("query") String query,
            @Param("state") String state,
            Pageable pageable
    );


}
