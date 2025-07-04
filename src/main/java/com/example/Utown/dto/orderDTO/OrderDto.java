package com.example.Utown.dto.orderDTO;

import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.enumFiles.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import com.example.Utown.model.DishElementToOrder;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.Client;
import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Restaurant restaurant;
    private Client client;
    private List<DishToOrder> dishesToOrder;
    private List<DishElementToOrder> dishElementToOrders;
}
