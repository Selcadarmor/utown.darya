package com.example.Utown.service;

import com.example.Utown.dto.ratingDTO.RatingDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Rating;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.RatingRepository;
import com.example.Utown.service.UserTypeService.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ClientService clientService;
    private final RestaurantService restaurantService;

    @Override
    @Transactional(readOnly = true)
    public Rating getRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", id));
    }

    @Override
    public List<Rating> getAll() {
        return ratingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rating> getAllRatingsByRestaurantId(Long restaurantId) {
        return ratingRepository.findAllByRestaurantId(restaurantId);
    }

    @Override
    @Transactional
    public void createRating(Long restaurantId, RatingDto ratingDto) {
        Client client = clientService.getCurrentClient();
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        Rating rating = Rating.builder()
                .client(client)
                .restaurant(restaurant)
                .grade(ratingDto.getGrade())
                .build();

        ratingRepository.save(rating);
    }

    @Override
    @Transactional
    public void deleteRating(Long id) {
        Rating rating = getRatingById(id);

        ratingRepository.delete(rating);
    }


}
