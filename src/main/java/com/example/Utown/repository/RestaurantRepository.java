package com.example.Utown.repository;

import com.example.Utown.dto.adminDto.OrderShortDto;
import com.example.Utown.dto.adminDto.RestaurantCreateUpdateDto;
import com.example.Utown.dto.adminDto.RestaurantDetailsDto;
import com.example.Utown.dto.adminDto.RestaurantInfoDto;
import com.example.Utown.dto.clientDto.RestaurantDto;
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
    List<OrderShortDto> findAllOrdersByRestaurantId(@Param("restaurantId") Long restaurantId);//метод получения всех заказов ресторана

    @Query("""
        SELECT new com.example.Utown.dto.adminDto.RestaurantInfoDto(
            r.id,
            r.title,
            r.phone,
            r.address.city,
            COUNT(o)
         )
        FROM Restaurant r
        JOIN r.orders o
        ORDER BY r.id, r.title, r.phone, r.address.city
    """)
    List<RestaurantInfoDto> findAllRestaurantInfos(); // метод получения всех ресторанов

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
            r.fileInfo.id,
            r.category.name
        )
        FROM Restaurant r
        LEFT JOIN r.orders o
        WHERE r.id = :id
        GROUP BY r.id, r.title, r.description, r.phone,
                  r.address.city, r.address.area, r.minOrderAmount,
                  r.fileInfo, r.category.name
""")
    Optional<RestaurantDetailsDto> findRestaurantDetailsById(@Param("id") Long id); // метод получения вей инфы об ресторане по id

    @Query("""
        SELECT new com.example.Utown.dto.adminDto.RestaurantCreateUpdateDto(
            r.id,
            r.title,
            r.description,
            r.phone,
            r.minOrderAmount,
            r.address.city,
            r.category.name,
            r.fileInfo.id
        )
        FROM Restaurant r
        WHERE r.id = :id
    """)
    Optional<RestaurantCreateUpdateDto> findRestaurantCreateUpdateDtoById(@Param("id") Long id);// метод  обнавдения и создания ресторана

    Long countByCategory(RestaurantCategory category);

    @Query("""
    select new com.example.Utown.dto.clientDto.RestaurantDto(
        r.fileInfo.path,
        r.title,
        null,
        d.price,
        r.deliveryTime
    )
    from Restaurant r
    join r.category c
    left join Delivery d on d.restaurant = r and d.isActive = true
""")
    List<RestaurantDto> findAllRestaurantsForClient();



}
