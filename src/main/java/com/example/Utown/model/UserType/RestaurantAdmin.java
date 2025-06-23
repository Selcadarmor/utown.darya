package com.example.Utown.model.UserType;

import com.example.Utown.model.Restaurant;
import com.example.Utown.model.Role;
import com.example.Utown.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "restaurant_admins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantAdmin extends User {

    @OneToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "restaurant_admin_roles",
            joinColumns = @JoinColumn(name = "restaurant_admin_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());
    }
}


