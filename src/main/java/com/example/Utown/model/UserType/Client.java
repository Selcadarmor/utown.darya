package com.example.Utown.model.UserType;

import com.example.Utown.model.*;
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


    @Column(name = "full_name", length = 170)
    private String fullName;

    @Column(name = "default_address")
    private Long defaultAddress;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private Set<Restaurant> favoriteRestaurants;

    @OneToMany(mappedBy = "client",  cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Set<Address> addresses;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private FileInfo fileInfo;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private List<Order> orders;

    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

}


