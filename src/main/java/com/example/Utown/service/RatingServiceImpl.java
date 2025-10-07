package com.example.Utown.service;

import com.example.Utown.dto.ratingDTO.RatingDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Dish;
import com.example.Utown.model.Rating;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.RatingRepository;
import com.example.Utown.service.UserTypeService.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ClientService clientService;
    private final DishService dishService;
    private final RestaurantService restaurantService;

    @Override
    @Transactional(readOnly = true)
    public Rating getRatingById(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", id));
        log.info("Retrieved rating with id {}", id);
        return rating;
    }

    @Override
    public List<Rating> getAll() {
        List<Rating> ratings = ratingRepository.findAll();
        log.info("Retrieved all ratings, count: {}", ratings.size());
        return ratings;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rating> getAllRatingsByRestaurantId(Long restaurantId) {
        List<Rating> ratings = ratingRepository.findAllByRestaurantId(restaurantId);
        log.info("Retrieved {} ratings for restaurantId {}", ratings.size(), restaurantId);
        return ratings;
    }

    @Override
    @Transactional
    public void createRatingByDish(Long dishId, RatingDto ratingDto) {
        Client client = clientService.getCurrentClient();
        Dish dish = dishService.getDishById(dishId);

        Rating rating = Rating.builder()
                .client(client)
                .dish(dish)
                .grade(ratingDto.getGrade())
                .build();

        ratingRepository.save(rating);

        dishService.updateDishRating(dish);

        log.info("Created rating for dishId {} by clientId {} with grade {}",
                dishId, client.getId(), rating.getGrade());
    }

    @Override
    @Transactional
    public void createRatingByRestaurant(Long restaurantId, RatingDto ratingDto) {
        Client client = clientService.getCurrentClient();
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        Rating rating = Rating.builder()
                .client(client)
                .restaurant(restaurant)
                .grade(ratingDto.getGrade())
                .build();

        ratingRepository.save(rating);

        restaurantService.updateRestaurantRating(restaurant);

        log.info("Created rating for restaurantId {} by clientId {} with grade {}",
                restaurantId, client.getId(), ratingDto.getGrade());
    }

    @Override
    @Transactional
    public void deleteRating(Long id) {
        Rating rating = getRatingById(id);
        ratingRepository.delete(rating);
        log.info("Deleted rating with id {}", id);
    }


}
