package com.example.Utown.repository;


import com.example.Utown.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT d FROM Delivery d WHERE d.restaurant.id = :restaurantId")
    List<Delivery> findByRestaurantId(@Param("restaurantId") Long restaurantId);

}

