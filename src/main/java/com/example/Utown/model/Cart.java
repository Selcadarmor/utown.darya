package com.example.Utown.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "carts")
@EntityListeners(AuditingEntityListener.class)
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
    @CreatedDate
    private LocalDateTime createAt;
    @LastModifiedDate
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
