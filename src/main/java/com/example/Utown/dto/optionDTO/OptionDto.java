package com.example.Utown.dto.optionDTO;

import com.example.Utown.model.Dish;
import com.example.Utown.model.Element;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionDto {
    private Long id;
    private String name;
    private boolean required;
    private Integer min;
    private boolean multiple;
    private Integer max;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Dish dish;
    private List<Element> elements;
}

