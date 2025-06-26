package com.example.Utown.model.UserType;

import com.example.Utown.model.Order;
import com.example.Utown.model.Role;
import com.example.Utown.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Client extends User {
    @Column(name = "full_name",length = 170)
    private String fullName;

    @Column(name = "default_address")
    private Long defaultAddress;

    @OneToMany(mappedBy = "client")
    private List<Order> orders;

}

