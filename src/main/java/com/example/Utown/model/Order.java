package com.example.Utown.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String area;
    private String city;
    private String clientPhone;
    private String date;

    @Column(precision = 10, scale = 2)
    private BigDecimal deliveryPrice;
    private String deliveryTime;
    private String details;
    private String fullAddress;
    private Boolean isPaid;
    private Float latitude;
    private Float longitude;
    private String noteForCourier;
    private String number;
    @Column(precision = 10, scale = 2)
    private BigDecimal orderPrice;
    private String payment;
    private String postCode;
    private String state;
    private String street;
    private String status;
    private String restaurantPhone;
    private String time;
    private String timeOfDelivery;
    private String timeOfAccepted;
    private String timeOfSending;
    @Column(precision = 15, scale = 2)
    private BigDecimal totalSum;
    private Integer typeAddress;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurants restaurant;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Integer cookingTime;
    private String deliveryStatus;
    private String endTimeOfCooking;
    private String intercomeCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
