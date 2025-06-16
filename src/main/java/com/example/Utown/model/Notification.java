package com.example.Utown.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private String text;
    private String time;
    private String title;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "is_successful")
    private Boolean isSuccessful;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
