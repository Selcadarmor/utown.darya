package com.example.Utown.repository;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT new com.example.Utown.dto.deliveryDTO.DeliveryDto(d.id, d.area, d.price, d.district, d.isActive, d.isDeleted, d.createdAt, d.updatedAt, d.restaurant.id) " +
            "FROM Delivery d WHERE d.id = :id")
    Optional<DeliveryDto> findDeliveryById(Long id);

    @Query("SELECT new com.example.Utown.dto.deliveryDTO.DeliveryDto(d.id, d.area, d.price, d.district, d.isActive, d.isDeleted, d.createdAt, d.updatedAt, d.restaurant.id) " +
            "FROM Delivery d")
    List<DeliveryDto> findAllDeliveries();

    @Modifying
    @Query("UPDATE Delivery d SET d.isActive = false WHERE d.restaurant.id = :restaurantId")
    void deactivateByRestaurantId(@Param("restaurantId") Long restaurantId);

}

