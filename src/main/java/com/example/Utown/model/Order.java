package com.example.Utown.model;

import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.enumFiles.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String area;

    @Column(name = "city")
    private String city;

    @Column(name = "client_phone")
    private String clientPhone;

    private String date;

    @Column(name = "delivery_price")
    private BigDecimal deliveryPrice;

    @Column(name = "delivery_time")
    private String deliveryTime;

    private String details;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "note_for_courier")
    private String noteForCourier;

    @Column(name = "number")
    private String number;

    @Column(name = "order_price")
    private BigDecimal orderPrice;

    private String payment;

    private String postcode;

    @Column(name = "restaurant_phone")
    private String restaurantPhone;

    private String state;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String street;

    private String time;

    @Column(name = "time_of_accepted")
    private String timeOfAccepted;

    @Column(name = "time_of_delivery")
    private String timeOfDelivery;

    @Column(name = "time_of_sending")
    private String timeOfSending;

    @Column(name = "total_sum")
    private BigDecimal totalSum;

    @Column(name = "type_address")
    private Integer typeAddress;

    @Column(name = "cooking_time")
    private Integer cookingTime;

    @Column(name = "delivery_status")
    private String deliveryStatus;

    @Column(name = "end_time_of_cooking")
    private String endTimeOfCooking;

    @Column(name = "intercom_code")
    private String intercomCode;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DishToOrder> dishesToOrder;

}