package com.example.pizzaorder.controller;

import com.example.pizzaorder.form.PizzaForm;
import com.example.pizzaorder.service.PizzaService;
import com.example.pizzaorder.entity.Pizza;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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

    @GetMapping("/{id}/edit")
    public String editPizza(
            @PathVariable Long id,
            Model model) {

        Pizza pizza = pizzaService
                .findPizzaById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Pizza not found"
                        )
                );

        PizzaForm pizzaForm = new PizzaForm();

        pizzaForm.setPizzaName(
                pizza.getPizzaName()
        );

        pizzaForm.setDescription(
                pizza.getDescription()
        );

        pizzaForm.setPrice(
                pizza.getPrice()
        );

        pizzaForm.setCategory(
                pizza.getCategory()
        );

        model.addAttribute(
                "pizzaId",
                id
        );

        model.addAttribute(
                "pizzaForm",
                pizzaForm
        );

        return "admin/pizza-edit";
    }

    @PostMapping("/{id}")
    public String updatePizza(
            @PathVariable Long id,
            @Valid
            @ModelAttribute("pizzaForm")
            PizzaForm pizzaForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "pizzaId",
                    id
            );

            return "admin/pizza-edit";
        }

        pizzaService.updatePizza(
                id,
                pizzaForm
        );

        redirectAttributes.addFlashAttribute(
                "message",
                "ピザを更新しました。ID: " + id
        );

        return "redirect:/admin/pizzas";
    }

    @PostMapping("/{id}/active")
    public String updateActive(
            @PathVariable Long id,
            @RequestParam Integer active,
            RedirectAttributes redirectAttributes) {

        pizzaService.updatePizzaActive(
                id,
                active
        );

        String message =
                active == 1
                        ? "ピザを有効化しました。ID: " + id
                        : "ピザを無効化しました。ID: " + id;

        redirectAttributes.addFlashAttribute(
                "message",
                message
        );

        return "redirect:/admin/pizzas";
    }
}