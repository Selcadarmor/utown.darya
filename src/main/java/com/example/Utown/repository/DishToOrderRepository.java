package com.example.Utown.repository;

import com.example.Utown.model.DishToOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishToOrderRepository extends JpaRepository<DishToOrder, Long> {

    @Query("SELECT d.title " +
            "FROM DishToOrder dto " +
            "JOIN dto.dish d " +
            "WHERE dto.order.id = :orderId")
    List<String> findDishTitleByOrderId(@Param("orderId") Long orderId);

}
