package com.example.Utown.repository;

import com.example.Utown.model.Restaurants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurants, Long> {

}
