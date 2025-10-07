package com.example.Utown.repository;

import com.example.Utown.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT d FROM Delivery d WHERE d.restaurant.id = :restaurantId AND d.isActive = true AND d.isDeleted = false")
    List<Delivery> findByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("SELECT d FROM Delivery d WHERE d.restaurant.id = :restaurantId AND d.isActive = false AND d.isDeleted = false")
    List<Delivery> findActiveByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("SELECT d FROM Delivery d WHERE d.restaurant.id = :restaurantId AND d.isActive = false AND d.isDeleted = true")
    List<Delivery> findDeletedByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM Delivery d " +
            "WHERE d.restaurant.id = :restaurantId " +
            "AND d.area = :area " +
            "AND d.district = :district " +
            "AND d.isDeleted = false")
    boolean existsByRestaurantAndAreaAndDistrict(@Param("restaurantId") Long restaurantId,
                                                 @Param("area") String area,
                                                 @Param("district") String district);

    @Query("SELECT COUNT(d) > 0 FROM Delivery d WHERE " +
            "d.restaurant.id = :restaurantId AND " +
            "d.area = :area AND d.district = :district AND " +
            "d.isDeleted = false AND d.id <> :id")
    boolean existsSimilarDelivery(@Param("restaurantId") Long restaurantId,
                                  @Param("area") String area,
                                  @Param("district") String district,
                                  @Param("id") Long id);


}

