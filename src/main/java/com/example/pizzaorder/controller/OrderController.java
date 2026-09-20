package com.example.pizzaorder.controller;

import com.example.pizzaorder.entity.Pizza;
import com.example.pizzaorder.form.OrderForm;
import com.example.pizzaorder.service.PizzaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

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

        OrderForm orderForm = new OrderForm();
        orderForm.setPizzaId(pizzaId);
        orderForm.setQuantity(1);

        model.addAttribute("pizza", pizza);
        model.addAttribute("orderForm", orderForm);

        return "order-form";
    }

    @PostMapping("/orders")
    public String createOrder(
            @Valid @ModelAttribute("orderForm") OrderForm orderForm,
            BindingResult bindingResult,
            Model model) {

        if (orderForm.getPizzaId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Pizza ID is required"
            );
        }

        Pizza pizza = pizzaService.findActivePizzaById(orderForm.getPizzaId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Pizza not found"
                        )
                );

        model.addAttribute("pizza", pizza);

        if (bindingResult.hasErrors()) {
            return "order-form";
        }

        Integer quantity = orderForm.getQuantity();

        BigDecimal total = pizza.getPrice()
                .multiply(BigDecimal.valueOf(quantity));

        model.addAttribute("quantity", quantity);
        model.addAttribute("total", total);

        return "order-confirm";
    }
}