package com.example.Utown.dto.dishDTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DishDeletedMenuDto {
    private Long id;
    private String title;
    private String description;
    private String categoryName;
    private BigDecimal price;
    private Long fileId;
    private String filePath;
    private Boolean isDeleted;

    public DishDeletedMenuDto(Long id, String title, String description, String categoryName, BigDecimal price, Long fileId, String filePath, Boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryName = categoryName;
        this.price = price;
        this.fileId = fileId;
        this.filePath = filePath;
        this.isDeleted = isDeleted;
    }
}
