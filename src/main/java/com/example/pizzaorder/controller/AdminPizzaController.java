package com.example.pizzaorder.controller;

import com.example.pizzaorder.form.PizzaForm;
import com.example.pizzaorder.service.PizzaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pizzas")
public class AdminPizzaController {

    private final PizzaService pizzaService;

    public AdminPizzaController(
            PizzaService pizzaService) {

        this.pizzaService = pizzaService;
    }

    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "pizzas",
                pizzaService.findAllPizzas()
        );

        return "admin/pizza-list";
    }

    @GetMapping("/new")
    public String newPizza(Model model) {

        model.addAttribute(
                "pizzaForm",
                new PizzaForm()
        );

        return "admin/pizza-form";
    }

    @PostMapping
    public String createPizza(
            @Valid
            @ModelAttribute("pizzaForm")
            PizzaForm pizzaForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "admin/pizza-form";
        }

        Long pizzaId =
                pizzaService.createPizza(pizzaForm);

        redirectAttributes.addFlashAttribute(
                "message",
                "ピザを登録しました。ID: " + pizzaId
        );

        return "redirect:/admin/pizzas";
    }
}