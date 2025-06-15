package com.example.Utown.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "favorites")
public class Favorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    @PrePersist
    public void onCreate() {
        this.createAt = LocalDateTime.now();
    }
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurants restaurant;
}
