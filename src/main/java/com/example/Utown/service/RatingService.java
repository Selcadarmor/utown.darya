package com.example.Utown.service;

import com.example.Utown.dto.ratingDTO.RatingDto;
import com.example.Utown.model.Rating;
import com.example.Utown.model.UserType.Client;

import java.util.List;

public interface RatingService {
    Rating getRatingById(Long id);
    List<Rating> getAll();
    List<Rating> getAllRatingsByRestaurantId(Long restaurantId);
    void createRating(Long restaurantId, RatingDto ratingDto);
    void deleteRating(Long id);
}
