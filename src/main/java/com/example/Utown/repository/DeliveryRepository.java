package com.example.Utown.repository;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT new com.example.Utown.dto.deliveryDTO.DeliveryDto(d.id, d.area, d.price, d.district, d.isActive, d.isDeleted, d.createdAt, d.updatedAt, d.restaurant.id) " +
            "FROM Delivery d WHERE d.id = :id")
    Optional<DeliveryDto> findDeliveryById(Long id);

    @Query("SELECT new com.example.Utown.dto.deliveryDTO.DeliveryDto(d.id, d.area, d.price, d.district, d.isActive, d.isDeleted, d.createdAt, d.updatedAt, d.restaurant.id) " +
            "FROM Delivery d")
    List<DeliveryDto> findAllDeliveries();
}

