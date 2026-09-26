package com.example.pizzaorder.repository;

import com.example.pizzaorder.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderIdOrderByOrderItemIdAsc(
            Long orderId
    );
}