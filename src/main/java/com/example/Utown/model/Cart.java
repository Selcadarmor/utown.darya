package com.example.Utown.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "delivery_price", precision = 10, scale = 2)
    private BigDecimal deliveryPrice;
    @Column(name = "sum_order",precision = 7, scale = 2)
    private BigDecimal sumOrder;
    @Column(name = "total_dish")
    private Integer totalDish;
    @Column(name = "total_sum", precision = 15, scale = 2)
    private BigDecimal totalSum;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
