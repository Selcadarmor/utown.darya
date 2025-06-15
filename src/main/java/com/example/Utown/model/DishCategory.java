package com.example.Utown.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "dish_category")
public class DishCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;
    private Integer sort;
    @Column(name = "is_active")
    private Boolean isActive;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurants restaurant;
    @ManyToOne
    @JoinColumn(name = "file_id")
    private FileInfo file;
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
    @PrePersist
    public void onCreate() {
        this.createdAt = java.time.LocalDateTime.now();
    }
}
