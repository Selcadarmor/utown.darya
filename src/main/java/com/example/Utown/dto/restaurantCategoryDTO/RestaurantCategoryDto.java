package com.example.Utown.dto.restaurantCategoryDTO;

import com.example.Utown.model.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCategoryDto {
    private Long id;
    private String name;
    private Integer sort;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private FileInfo file;
}