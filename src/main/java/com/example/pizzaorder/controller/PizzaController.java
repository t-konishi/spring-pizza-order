package com.example.pizzaorder.controller;

import com.example.pizzaorder.entity.Pizza;
import com.example.pizzaorder.service.PizzaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
