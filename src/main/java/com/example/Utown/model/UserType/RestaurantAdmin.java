package com.example.Utown.model.UserType;

import com.example.Utown.model.Restaurant;
import com.example.Utown.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantAdmin extends User {

    @OneToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "full_name", length = 170)
    private String fullName;

    @Column(name = "default_address")
    private Long defaultAddress;

}

