package com.example.Utown.model.UserType;

import com.example.Utown.model.Order;
import com.example.Utown.model.Role;
import com.example.Utown.model.Address;
import com.example.Utown.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


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

    @ManyToOne
    @JoinColumn(name = "default_address_id")
    private Address defaultAddress;

}


