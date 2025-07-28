package com.example.Utown.repository;

import com.example.Utown.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r FROM Rating r WHERE r.restaurant.id = :restaurantId")
    List<Rating> findAllByRestaurantId(@Param("restaurantId") Long restaurantId);

    Optional<Rating> findByClientIdAndRestaurantId(Long clientId, Long restaurantId);

}
