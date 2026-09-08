package com.example.pizzaorder.repository;

import com.example.pizzaorder.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    List<Pizza> findByActiveOrderByPizzaIdAsc(Integer active);

    Optional<Pizza> findByPizzaIdAndActive(Long pizzaId, Integer active);
}
