package com.example.pizzaorder.repository;

import com.example.pizzaorder.entity.PizzaOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaOrderRepository
        extends JpaRepository<PizzaOrder, Long> {
}