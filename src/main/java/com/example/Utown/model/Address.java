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

   @Column(length = 100, nullable = false)
    private String area;
   @Column(length = 100, nullable = false)
    private String city;
   @Column(length = 100, nullable = false)
    private String details;
    @Column(name = "full_address")
    private String fullAddress;
    @Column(name = "latitube")
    private Float latitude;
    @Column(name = "longitube")
    private Float longitude;
    @Column(name = "post_code")
    private String postCode;
    @Column(name = "state")
    private String state;
    @Column(length = 100, nullable = false)
    private String street;

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

    @OneToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToMany(mappedBy = "addresses")
    private Set<User> users;
}
