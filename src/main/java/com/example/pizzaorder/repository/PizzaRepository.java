package com.example.pizzaorder.repository;

import com.example.pizzaorder.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    List<Pizza> findByActiveOrderByPizzaIdAsc(Integer active);
}
