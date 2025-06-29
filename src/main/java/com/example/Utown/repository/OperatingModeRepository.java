package com.example.Utown.repository;

import com.example.Utown.model.OperatingMode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface OperatingModeRepository extends JpaRepository<OperatingMode, Long> {
    List<OperatingMode> findByRestaurantId(Long restaurantId);
}
