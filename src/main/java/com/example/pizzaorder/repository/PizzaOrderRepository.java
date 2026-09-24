package com.example.pizzaorder.repository;

import com.example.pizzaorder.entity.PizzaOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PizzaOrderRepository
        extends JpaRepository<PizzaOrder, Long> {

    List<PizzaOrder> findAllByOrderByOrderIdDesc();
}