package com.example.Utown.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private String date;

    @Column(name = "type_notification")
    private Integer typeNotification;

    @Column(name = "type_order")
    private Integer typeOrder;

    @Column(name = "type_restaurant")
    private Integer typeRestaurant;

    @Column(name = "type_user")
    private Integer typeUser;

    @Column(name = "text")
    private String text;

    @Column(name = "time_notification")
    private String timeNotification;

    @Column(name = "time_order")
    private String timeOrder;

    @Column(name = "time_restaurant")
    private String timeRestaurant;

    @Column(name = "time_user")
    private String timeUser;

    @Column(name = "time_order_status")
    private String time;

    @Column(name = "type_order_title")
    private String title;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "is_successful")
    private Boolean isSuccessful;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
