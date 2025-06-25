package com.example.Utown.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public abstract class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fcm_token", length = 600)
    private String fcmToken;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "password", length = 255, nullable = false)
    @NotBlank
    private String password;

    @Column(name = "phoneNumber", length = 20, unique = true, nullable = false)
    @NotBlank
    private String username;

    @Column(name = "full_name", length = 170)
    private String fullName;

    @Column(name = "transport", length = 50)
    private String transport;

    @Column(name = "address_id")
    private Long addressId;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @Column(name = "platform")
    private boolean platform;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private Set<Restaurant>  favoriteRestaurants;


    @ManyToOne
    @JoinColumn(name = "default_address_")
    private Address defaultAddress;


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}



