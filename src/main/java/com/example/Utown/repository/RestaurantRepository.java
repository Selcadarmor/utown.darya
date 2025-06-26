package com.example.Utown.repository;

import com.example.Utown.dto.adminDto.OrderShortDto;
import com.example.Utown.dto.adminDto.RestaurantDetailsDto;
import com.example.Utown.dto.adminDto.RestaurantInfoDto;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("""
        SELECT new com.example.Utown.dto.adminDto.OrderShortDto(
            o.id,
            o.totalSum,
            o.createdAt,
            o.status
        )
        FROM Order o 
        WHERE o.restaurant.id = :restaurantId
        ORDER BY o.createdAt DESC            
    """)
    List<OrderShortDto> findAllOrdersByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("""
        SELECT new com.example.Utown.dto.adminDto.RestaurantInfoDto(
            r.id,
            r.title,
            r.phone,
            r.address.city,
            r.category,
            COUNT(o)
         )
        FROM Restaurant r
        JOIN r.orders o
        ORDER BY r.id, r.title, r.phone, r.address.city, r.category
    """)
    List<RestaurantInfoDto> findAllRestaurantInfos();

    @Query(""" 
        SELECT new com.example.Utown.dto.adminDto.RestaurantDetailsDto(
            r.id,
            r.title,
            r.description,
            r.phone,
            r.address.city,
            r.address.area,
            r.minOrderAmount,
            COUNT(o),
            r.fileInfo.id
        )
        FROM Restaurant r
        LEFT JOIN r.orders o
        WHERE r.id = :id
        GROUP BY r.id, r.title, r.description, r.phone,
                  r.address.city, r.address.area, r.minOrderAmount,
                  r.fileInfo
""")
    Optional<RestaurantDetailsDto> findRestaurantDetailsById(@Param("id") Long id);

    Long countByCategory(RestaurantCategory category);
}
