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
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "dishes_to_order")
@EntityListeners(AuditingEntityListener.class)
public class DishToOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "count")
    private Integer count;

    @Column(name = "sum", precision = 12, scale = 2)
    private BigDecimal sum;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @ManyToOne
    @JoinColumn(name = "element_id")
    private Element element;

    @ManyToOne
    @JoinColumn(name = "order_id_in_dish")
    private Order order;

}
