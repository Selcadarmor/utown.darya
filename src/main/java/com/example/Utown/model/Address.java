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
import java.util.Set;

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
    @CreatedDate
    private LocalDateTime createAt;
    @LastModifiedDate
    private LocalDateTime updateAt;

    @OneToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToMany(mappedBy = "address")
    private Set<User> users;
}
