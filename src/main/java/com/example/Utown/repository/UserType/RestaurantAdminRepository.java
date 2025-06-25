package com.example.Utown.repository.UserType;

import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.UserType.RestaurantAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantAdminRepository extends JpaRepository<RestaurantAdmin, Long> {
    Optional<RestaurantAdmin> findByUsername(String username);
}
