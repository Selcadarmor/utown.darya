package com.example.Utown.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "dishes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 700)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "sort")
    private Integer sort;

    @Column(length = 170, nullable = false)
    private String title;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "file_id")
    private FileInfo file;

    @ManyToOne
    @JoinColumn(name = "dish_category_id")
    private DishCategory dishCategory;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "dish",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;

}