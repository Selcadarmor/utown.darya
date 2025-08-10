package com.example.Utown.model;


import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.model.enumFiles.RestaurantStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(
        name = "Restaurant.detail",
        attributeNodes = {
                @NamedAttributeNode("categories"),
                @NamedAttributeNode("operatingModes"),
                @NamedAttributeNode("deliveries"),
                @NamedAttributeNode("fileInfo"),
                @NamedAttributeNode("restaurantAdmin")
        }
)
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

    @Column(name = "restaurant_status")
    @Enumerated(EnumType.STRING)
    private RestaurantStatus status;

    @Column(length = 170)
    private  String title;

    private Integer totalRatings;

    private Boolean statusForcedChanged;

    private Boolean isActive;

    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private RestaurantAdmin restaurantAdmin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "restaurant_categories_link",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<RestaurantCategory> categories;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Delivery> deliveries;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OperatingMode> operatingModes;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "file_id")
    private FileInfo fileInfo;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DishCategory> dishCategories;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
