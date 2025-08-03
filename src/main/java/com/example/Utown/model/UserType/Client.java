package com.example.Utown.model.UserType;

import com.example.Utown.model.Address;
import com.example.Utown.model.Cart;
import com.example.Utown.model.FileInfo;
import com.example.Utown.model.Order;
import com.example.Utown.model.Restaurant;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Set<Address> addresses;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private FileInfo fileInfo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private List<Order> orders;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

}