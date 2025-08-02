package com.example.Utown.repository;

import com.example.Utown.model.Order;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.enumFiles.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= :start AND o.createdAt < :end")
    long countByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    List<Order> findByRestaurant(Restaurant restaurant);

    List<Order> findByRestaurantAndStatus(Restaurant restaurant, OrderStatus status);

    @Query("SELECT o FROM Order o " +
            "JOIN o.client c " +
            "JOIN o.restaurant r " +
            "WHERE (:query IS NULL OR " +
            "LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "c.username LIKE CONCAT('%', :query, '%') OR " +
            "LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "r.phone LIKE CONCAT('%', :query, '%') OR " +
            "o.number LIKE CONCAT('%', :query, '%')) " )
    Page<Order> findAllWithFilter(@Param("query") String query,
                                  @P("clientId") Long clientId, Pageable pageable);

    List<Order> findAllByRestaurantIdAndDateBetween(Long restaurantId, LocalDate start, LocalDate end);

    List<Order> findAllByRestaurantIdAndDate(Long restaurantId, LocalDate date);
}
