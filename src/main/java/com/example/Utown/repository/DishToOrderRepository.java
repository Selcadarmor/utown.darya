package com.example.Utown.repository;

import com.example.Utown.model.DishToOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishToOrderRepository extends JpaRepository<DishToOrder, Long> {
}
