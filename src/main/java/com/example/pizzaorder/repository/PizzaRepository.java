package com.example.pizzaorder.repository;

import com.example.pizzaorder.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {
}
