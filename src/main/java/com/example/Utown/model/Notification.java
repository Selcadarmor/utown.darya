package com.example.Utown.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String LocalDate;
    @Column(columnDefinition = "LONGTEXT")
    private String text;
    private String LocalTime;
    private String title;
    private String errorMessage;
    private Boolean isSuccessful;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    @PrePersist
    public void onCreate() {
        this.createAt= LocalDateTime.now();
    }
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
