package com.example.Utown.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state")
     private String state; //административная область = 서울특별시, 경기도

    @Column(length = 100, nullable = false)
    private String city; //город или район = 강남구, 안성시
                         // district в Delivery
   @Column(length = 100, nullable = false)
    private String area; //микрорайон, (동), или просто часть города например, 역삼동, 삼성동

   @Column(length = 100, nullable = false)
    private String street;

   @Column(length = 100, nullable = false)
    private String details;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "intercome_code", length = 100, nullable = false)
    private String intercomCode;

    @Column(name = "type_address")
    private Integer typeAddress;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}