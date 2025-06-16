package com.example.Utown.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(length = 30)
    private String category;
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
    private Integer status;
    @Column(length = 170)
    private  String title;
    private Integer totalRatings;
    private boolean statusForcedChanged;
    private boolean isActive;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @PrePersist
    public void onCreate() {
        this.createTime = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "adress_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "fail_id")
    private FileInfo failInfo;

    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.PERSIST)
    private List<OperatingMode> operatingModes;
}
