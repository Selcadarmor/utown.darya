package com.example.Utown.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String area;
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    @Column(name = "district", length = 50)
    private String district;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
    @PrePersist
    public void onCreate() {
        this.createdAt = java.time.LocalDateTime.now();
    }
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurants restaurant;
}
