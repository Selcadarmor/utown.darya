package com.example.demo.repository;

import com.example.demo.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    List<FileInfo> findByDishId(Long dishId);
    List<FileInfo> findByRestaurantId(Long restaurantId);
}
