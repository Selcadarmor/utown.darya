package com.example.Utown.model.UserType;

import com.example.Utown.model.Restaurant;
import com.example.Utown.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "restaurant_admins")
@AllArgsConstructor
public class RestaurantAdmin extends User {
    @OneToOne
    @JoinColumn(name = "restaurant_admin_id")
    private Restaurant restaurant;
}
