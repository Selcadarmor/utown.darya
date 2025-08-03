package com.example.Utown.config;

import com.example.Utown.model.DishCategory;
import com.example.Utown.repository.DishCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitialize {
    @Bean
    public CommandLineRunner initDefaultCategory(DishCategoryRepository repository) {
        return args -> {
            repository.findByName("No category").orElseGet(() -> {
                DishCategory category = new DishCategory();
                category.setName("No category");
                category.setSort(0);
                category.setIsActive(true);
                return repository.save(category);
            });
        };
    }
}
