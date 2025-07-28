package com.example.Utown.service;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import java.util.List;

import com.example.Utown.dto.deliveryDTO.DeliveryInfoDto;
import com.example.Utown.model.Delivery;
import com.example.Utown.model.Restaurant;

public interface DeliveryService {
    Delivery getDeliveryById(Long id);
    List<Delivery> getAllDeliveries();
    List<DeliveryInfoDto> getDeliveriesByRestaurantId(Long restaurantId);
    Delivery createDelivery(DeliveryDto dto);
    void updateDeliveriesByRestaurant(Restaurant restaurant, List<DeliveryDto> dtos);
    Delivery updateDelivery(Long id, DeliveryDto dto);
    void deleteDelivery(Long id);
}


