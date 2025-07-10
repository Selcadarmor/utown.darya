package com.example.Utown.repository;

import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.model.OperatingMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface OperatingModeRepository extends JpaRepository<OperatingMode, Long> {
    List<OperatingMode> findByRestaurantId(Long restaurantId);

   @Query("SELECT new com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto " +
           "(o.id, o.start, o.end, o.dayOff, o.dayOfWeek, o.createdAt, o.updatedAt, o.restaurant.id) " +
           "FROM OperatingMode o ")
    List<OperatingModeInfoDto> findAllOperatingModes();

   @Query("SELECT new com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto " +
           "(o.id, o.start, o.end, o.dayOff, o.dayOfWeek, o.createdAt, o.updatedAt, o.restaurant.id) " +
           "FROM OperatingMode o WHERE o.id =:id ")
   Optional<OperatingModeInfoDto> findProjectedById (@Param("id")Long id);

}
