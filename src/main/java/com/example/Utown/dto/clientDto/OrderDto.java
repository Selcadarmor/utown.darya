package com.example.Utown.dto.clientDto;

import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.enumFiles.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;

    private String area;
    private String city;
    private String clientPhone;
    private String date;
    private BigDecimal deliveryPrice;
    private String deliveryTime;
    private String details;
    private String fullAddress;
    private Boolean isPaid;
    private Float latitude;
    private Float longitude;
    private String noteForCourier;
    private String number;
    private BigDecimal orderPrice;
    private String payment;
    private String postcode;
    private String restaurantPhone;
    private String state;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String street;
    private String time;
    private String timeOfAccepted;
    private String timeOfDelivery;
    private String timeOfSending;
    private BigDecimal totalSum;
    private Integer typeAddress;
    private Integer cookingTime;
    private String deliveryStatus;
    private String endTimeOfCooking;
    private String intercomCode;
    private RestaurantDto restaurantDto;
    private List<DishToOrder> dishToOrder;
}
