package com.example.Utown.controller;


import com.example.Utown.dto.clientDto.RestaurantCategoryDto;
import com.example.Utown.service.RestaurantCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Tag(name = "Client", description = "Client specific operations")
public class ClientController {

    private RestaurantCategoryService restaurantCategoryService;

    @GetMapping("/restaurant_categories")
    public ResponseEntity<List<RestaurantCategoryDto>> getAllCategories() {
        return ResponseEntity.ok(restaurantCategoryService.getAllCategoriesWithCount());
    }


}

