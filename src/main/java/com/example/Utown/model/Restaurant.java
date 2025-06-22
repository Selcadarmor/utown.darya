package com.example.Utown.model;

import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.model.enumFiles.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(length = 10)
    private String deliveryTime;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    @Column(length = 255)
    private String facilities;
    private Boolean isRecommended;
    @Column(precision = 10, scale = 2)
    private BigDecimal minOrderAmount;
    @Column(length = 20)
    private String phone;
    @Column(precision = 15, scale = 2)
    private BigDecimal rating;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(length = 170)
    private  String title;
    private Integer totalRatings;
    private Boolean statusForcedChanged;
    private Boolean isActive;
    @CreatedDate
    private LocalDateTime createAt;
    @LastModifiedDate
    private LocalDateTime updateAt;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne
    @JoinColumn(name = "restaurant_admin_id")
    private RestaurantAdmin restaurantAdmin;


    @OneToOne
    @JoinColumn(name = "file_id")
    private FileInfo fileInfo;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperatingMode> operatingModes;
}
