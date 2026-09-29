package com.example.pizzaorder.service;

import com.example.pizzaorder.entity.Pizza;
import com.example.pizzaorder.repository.PizzaRepository;
import com.example.pizzaorder.form.PizzaForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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

    @Transactional(readOnly = true)
    public List<Pizza> findAllPizzas() {
        return pizzaRepository.findAllByOrderByPizzaIdAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Pizza> findPizzaById(Long pizzaId) {
        return pizzaRepository.findById(pizzaId);
    }

    @Transactional
    public Long createPizza(PizzaForm form) {

        Pizza pizza = new Pizza();

        pizza.setPizzaName(form.getPizzaName());
        pizza.setDescription(form.getDescription());
        pizza.setPrice(form.getPrice());
        pizza.setCategory(form.getCategory());

        pizza.setActive(1);

        LocalDateTime now =
                LocalDateTime.now(ZoneOffset.UTC);

        pizza.setCreatedAt(now);
        pizza.setUpdatedAt(now);

        Pizza savedPizza =
                pizzaRepository.save(pizza);

        return savedPizza.getPizzaId();
    }

    @Transactional
    public void updatePizza(
            Long pizzaId,
            PizzaForm form) {

        Pizza pizza = pizzaRepository
                .findById(pizzaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pizza not found: " + pizzaId
                        )
                );

        pizza.setPizzaName(form.getPizzaName());
        pizza.setDescription(form.getDescription());
        pizza.setPrice(form.getPrice());
        pizza.setCategory(form.getCategory());
    }
}