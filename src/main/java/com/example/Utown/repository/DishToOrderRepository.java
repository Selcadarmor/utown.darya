package com.example.Utown.repository;

import com.example.Utown.dto.dishToOrderDTO.DishInCartDto;
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

    @Query("SELECT new com.example.Utown.dto.dishToOrderDTO.DishInCartDto(" +
            "d.id, d.title, d.description, " +
            "f.path, " +
            "dto.sum, dto.count, dto.sum) " +
            "FROM DishToOrder dto " +
            "JOIN dto.dish d " +
            "LEFT JOIN d.file f " +
            "WHERE dto.cart.id = :cartId")
    List<DishInCartDto> findDishesInCartByCartId(@Param("cartId") Long cartId);



}
