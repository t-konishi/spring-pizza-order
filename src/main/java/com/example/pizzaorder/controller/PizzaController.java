package com.example.pizzaorder.controller;

import com.example.pizzaorder.entity.Pizza;
import com.example.pizzaorder.service.PizzaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class PizzaController {

    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping("/pizzas")
    public String list(Model model) {

        List<Pizza> pizzas = pizzaService.findActivePizzas();

        model.addAttribute("pizzas", pizzas);

        return "pizzas";
    }

    @GetMapping("/pizzas/{id}")
    public String detail(@PathVariable Long id, Model model) {

        Pizza pizza = pizzaService.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Pizza not found"
                        )
                );

        model.addAttribute("pizza", pizza);

        return "pizza-detail";
    }
}
