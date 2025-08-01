package com.example.Utown.dto.dishDTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DishMenuDto {
    private Long id;
    private String title;
    private String description;
    private String categoryName;
    private Boolean isActive;
    private BigDecimal price;
    private Long fileId;
    private String filePath;
    private String fileUrl;
    private Boolean isDeleted;

    public DishMenuDto(Long id, String title, String description, String categoryName, Boolean isActive, BigDecimal price, Long fileId, String filePath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryName = categoryName;
        this.isActive = isActive;
        this.price = price;
        this.fileId = fileId;
        this.filePath = filePath;
    }

}
