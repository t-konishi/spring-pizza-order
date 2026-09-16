package com.example.pizzaorder.controller;

import com.example.pizzaorder.entity.Pizza;
import com.example.pizzaorder.service.PizzaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class OrderController {

    private final PizzaService pizzaService;

    public OrderController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping("/orders/new")
    public String newOrder(
            @RequestParam Long pizzaId,
            Model model) {

        Pizza pizza = pizzaService.findActivePizzaById(pizzaId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Pizza not found"
                        )
                );

        model.addAttribute("pizza", pizza);

        return "order-form";
    }
}
