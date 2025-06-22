package com.example.Utown.dto;

import com.example.Utown.model.DishCategory;
import com.example.Utown.model.FileInfo;
import com.example.Utown.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDto {
    private Long id;
    private String description;
    private Boolean isActive;
    private Boolean isDeleted;
    private BigDecimal price;
    private Integer sort;
    private String title;
    private FileInfo file;
    private DishCategory dishCategory;
    private Restaurant restaurant;

}
