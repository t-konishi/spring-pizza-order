package com.example.pizzaorder.repository;

import com.example.pizzaorder.dto.OrderHistoryDto;
import com.example.pizzaorder.dto.OrderDetailDto;
import com.example.pizzaorder.entity.PizzaOrder;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PizzaOrderRepository
        extends JpaRepository<PizzaOrder, Long> {

    @Query("""
        SELECT new com.example.pizzaorder.dto.OrderHistoryDto(
            o.orderId,
            c.customerName,
            o.orderStatus,
            o.orderChannel,
            o.orderTotal,
            o.orderedAt
        )
        FROM PizzaOrder o
        JOIN Customer c
          ON c.customerId = o.customerId
        ORDER BY o.orderId DESC
        """)
    List<OrderHistoryDto> findOrderHistory();

    @Query("""
    SELECT new com.example.pizzaorder.dto.OrderDetailDto(
        o.orderId,
        c.customerId,
        c.customerName,
        c.email,
        o.orderStatus,
        o.orderChannel,
        o.orderTotal,
        o.orderedAt
    )
    FROM PizzaOrder o
    JOIN Customer c
      ON c.customerId = o.customerId
    WHERE o.orderId = :orderId
    """)
    Optional<OrderDetailDto> findOrderDetailById(
            @Param("orderId") Long orderId
    );
}