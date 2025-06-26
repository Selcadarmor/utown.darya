package com.example.Utown.model.UserType;

import com.example.Utown.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class RestaurantAdmin extends User {
    @Column(name = "full_name",length = 170)
    private String fullName;

    @Column(name = "default_address")
    private Long defaultAddress;

}
