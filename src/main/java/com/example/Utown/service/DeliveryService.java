package com.example.Utown.service;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import java.util.List;

import com.example.Utown.dto.deliveryDTO.DeliveryInfoDto;
import com.example.Utown.model.Delivery;
import com.example.Utown.model.Restaurant;

public interface DeliveryService {
    Delivery getDeliveryById(Long id);
    List<DeliveryDto> getAllDeliveriesByRestaurantId(Long restaurantId);
    List<DeliveryDto> getAllInActiveDeliveriesByRestaurantId(Long restaurantId);
    List<DeliveryDto> getAllDeletedDeliveriesByRestaurantId(Long restaurantId);
    List<DeliveryInfoDto> getDeliveriesByRestaurantId(Long restaurantId);
    Delivery createDelivery(DeliveryDto dto);
    Delivery addDelivery(DeliveryDto dto);
    void updateDeliveriesByRestaurant(Restaurant restaurant, List<DeliveryDto> dtos);
    Delivery updateDelivery(Long id, DeliveryDto dto);
    void reactivateDelivery(Long id);
    void stopDelivery(Long id);
    void deleteDelivery(Long id);
}


