package com.example.Utown.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String area;
    private String city;
    private String details;
    @Column(name = "full_address")
    private String fullAddress;
    private Float latitude;
    private Float longitude;
    private String postCode;
    private String state;
    private String street;

    @Column(name = "type_address")
    private Integer typeAddress;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    @PrePersist
    public void onCreate() {
        this.createAt = LocalDateTime.now();
    }
}
