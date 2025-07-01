package com.example.Utown.model.UserType;

import com.example.Utown.model.Address;
import com.example.Utown.model.Order;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@Entity
@DiscriminatorValue("client")
@Getter
@Setter
@NoArgsConstructor
public class Client extends User {

    @OneToMany(mappedBy = "client")
    private List<Order> orders;

    @Column(name = "full_name", length = 170)
    private String fullName;

    @Column(name = "default_address")
    private Long defaultAddress;

    @Column(name = "phone", length = 20)
    private String phone;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private Set<Restaurant> favoriteRestaurants;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_addresses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private Set<Address> addresses;
}


