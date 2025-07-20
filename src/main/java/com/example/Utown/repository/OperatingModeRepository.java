package com.example.Utown.repository;

import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeRestaurantProfileDto;
import com.example.Utown.model.OperatingMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperatingModeRepository extends JpaRepository<OperatingMode, Long> {
    List<OperatingMode> findByRestaurantId(Long restaurantId);

   @Query("SELECT new com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto " +
           "(o.startTime, o.endTime, o.dayOff, o.dayOfWeek, o.restaurant.id) " +
           "FROM OperatingMode o ")
    List<OperatingModeInfoDto> findAllOperatingModes();

   @Query("SELECT new com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto " +
           "(o.startTime, o.endTime, o.dayOff, o.dayOfWeek, o.restaurant.id) " +
           "FROM OperatingMode o WHERE o.id =:id ")
   Optional<OperatingModeInfoDto> findProjectedById (@Param("id")Long id);

    @Query("SELECT new com.example.Utown.dto.operatingModeDTO.OperatingModeRestaurantProfileDto(" +
            "o.dayOfWeek, o.startTime, o.endTime, o.dayOff) " +
            "FROM OperatingMode o " +
            "WHERE o.restaurant.id = :restaurantId " +
            "ORDER BY o.dayOfWeek")
    List<OperatingModeRestaurantProfileDto> findRawOperatingModesByRestaurantId(@Param("restaurantId") Long restaurantId);

}
