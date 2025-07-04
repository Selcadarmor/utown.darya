package com.example.Utown.service;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Delivery;
import com.example.Utown.model.Restaurant;
import com.example.Utown.repository.DeliveryRepository;
import com.example.Utown.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public Delivery createDelivery(DeliveryDto dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", dto.getRestaurantId()));

        Delivery delivery = Delivery.builder()
                .area(dto.getArea())
                .price(dto.getPrice())
                .district(dto.getDistrict())
                .isActive(dto.getIsActive())
                .isDeleted(dto.getIsDeleted())
                .restaurant(restaurant)
                .build();

        return deliveryRepository.save(delivery);
    }

    @Override
    public DeliveryDto getDeliveryById(Long id) {
        return deliveryRepository.findDeliveryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found", id));
    }

    @Override
    public List<DeliveryDto> getAllDeliveries() {
        return deliveryRepository.findAllDeliveries();
    }

    @Override
    public Delivery updateDelivery(Long id, DeliveryDto dto) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", id));

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", dto.getRestaurantId()));

        delivery.setArea(dto.getArea());
        delivery.setPrice(dto.getPrice());
        delivery.setDistrict(dto.getDistrict());
        delivery.setIsActive(dto.getIsActive());
        delivery.setIsDeleted(dto.getIsDeleted());
        delivery.setRestaurant(restaurant);

        return deliveryRepository.save(delivery);
    }

    @Override
    public void deleteDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found", id));
        deliveryRepository.delete(delivery);
    }
}

