package com.example.Utown.service;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.dto.deliveryDTO.DeliveryInfoDto;
import com.example.Utown.exception.InvalidArgumentException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.DeliveryMapper;
import com.example.Utown.model.Delivery;
import com.example.Utown.model.Restaurant;
import com.example.Utown.repository.DeliveryRepository;
import com.example.Utown.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final RestaurantRepository restaurantRepository;

    @Override
    public Delivery getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", id));
        return delivery;
    }

    @Override
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @Override
    public List<DeliveryInfoDto> getDeliveriesByRestaurantId(Long restaurantId) {
        List<Delivery> deliveries = deliveryRepository.findByRestaurantId(restaurantId);
        return deliveryMapper.toDtoList(deliveries);

    }

    @Override
    public Delivery createDelivery(DeliveryDto dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", dto.getRestaurantId()));

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
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateDeliveriesByRestaurant(Restaurant restaurant, List<DeliveryDto> dtos) {
        Map<Long, Delivery> existingDeliveriesById = restaurant.getDeliveries().stream()
                .filter(d -> d.getId() != null)
                .collect(Collectors.toMap(Delivery::getId, Function.identity()));

        for (DeliveryDto dto : dtos) {
            Long id = dto.getId();
            if (id == null) {
                throw new InvalidArgumentException( "deliveryId", null);
            }
            Delivery delivery = existingDeliveriesById.get(id);
            if (delivery == null) {
                throw new ResourceNotFoundException("Delivery", id);
            }

            // Обновляем поля
            delivery.setArea(dto.getArea());
            delivery.setDistrict(dto.getDistrict());
            delivery.setPrice(dto.getPrice());
            delivery.setIsActive(dto.getIsActive());
            delivery.setIsDeleted(dto.getIsDeleted());
        }

        deliveryRepository.saveAll(existingDeliveriesById.values());
    }

    @Override
    public Delivery updateDelivery(Long id, DeliveryDto dto) {
        Delivery delivery = getDeliveryById(id);

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", dto.getRestaurantId()));

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
        Delivery delivery = getDeliveryById(id);
        delivery.setIsDeleted(true);
        deliveryRepository.delete(delivery);
    }

}

