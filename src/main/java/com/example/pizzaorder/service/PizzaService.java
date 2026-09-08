package com.example.pizzaorder.service;

import com.example.pizzaorder.entity.Pizza;
import com.example.pizzaorder.repository.PizzaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {

    private final PizzaRepository pizzaRepository;

    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @Transactional(readOnly = true)
    public List<Pizza> findActivePizzas() {
        return pizzaRepository.findByActiveOrderByPizzaIdAsc(1);
    }

    @Transactional(readOnly = true)
    public Optional<Pizza> findActivePizzaById(Long pizzaId) {
        return pizzaRepository.findByPizzaIdAndActive(pizzaId, 1);
    }
}