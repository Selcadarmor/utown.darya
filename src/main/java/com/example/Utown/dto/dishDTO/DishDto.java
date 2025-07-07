package com.example.Utown.dto.dishDTO;

import com.example.Utown.model.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishDto {
    private Long id;
    private String description;
    private Boolean isActive;
    private Boolean isDeleted;
    private BigDecimal price;
    private Integer sort;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long restaurantId;
    private Long dishCategoryId;
    private Long fileId;
    private List<Option> options; //Не знаю что вам понадобиться если нужно меняйте

    public DishDto(Long id, String description, Boolean isActive, Boolean isDeleted,
                   BigDecimal price, Integer sort, String title,
                   LocalDateTime createdAt, LocalDateTime updatedAt,
                   Long restaurantId, Long dishCategoryId, Long fileId) {
        this.id = id;
        this.description = description;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.price = price;
        this.sort = sort;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.restaurantId = restaurantId;
        this.dishCategoryId = dishCategoryId;
        this.fileId = fileId;
    }

}
