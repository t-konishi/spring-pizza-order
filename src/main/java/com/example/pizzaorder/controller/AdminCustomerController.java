package com.example.pizzaorder.controller;

import com.example.pizzaorder.form.CustomerForm;
import com.example.pizzaorder.service.CustomerService;
import com.example.pizzaorder.entity.Customer;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/admin/customers")
public class AdminCustomerController {

    private final CustomerService customerService;

    public AdminCustomerController(
            CustomerService customerService) {

        this.customerService = customerService;
    }

    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "customers",
                customerService.findAllCustomers()
        );

        return "admin/customer-list";
    }

    @GetMapping("/new")
    public String newCustomer(Model model) {

        model.addAttribute(
                "customerForm",
                new CustomerForm()
        );

        return "admin/customer-form";
    }

    @PostMapping
    public String createCustomer(
            @Valid
            @ModelAttribute("customerForm")
            CustomerForm customerForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "admin/customer-form";
        }

        try {

            Long customerId =
                    customerService.createCustomer(
                            customerForm
                    );

            redirectAttributes.addFlashAttribute(
                    "message",
                    "顧客を登録しました。ID: "
                            + customerId
            );

        } catch (IllegalArgumentException e) {

            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    e.getMessage()
            );

            return "admin/customer-form";
        }

        return "redirect:/admin/customers";
    }

    @GetMapping("/{id}/edit")
    public String editCustomer(
            @PathVariable Long id,
            Model model) {

        Customer customer = customerService
                .findCustomerById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Customer not found"
                        )
                );

        CustomerForm customerForm =
                new CustomerForm();

        customerForm.setCustomerName(
                customer.getCustomerName()
        );

        customerForm.setEmail(
                customer.getEmail()
        );

        customerForm.setPhone(
                customer.getPhone()
        );

        model.addAttribute(
                "customerId",
                id
        );

        model.addAttribute(
                "customerForm",
                customerForm
        );

        return "admin/customer-edit";
    }

    @PostMapping("/{id}")
    public String updateCustomer(
            @PathVariable Long id,
            @Valid
            @ModelAttribute("customerForm")
            CustomerForm customerForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "customerId",
                    id
            );

            return "admin/customer-edit";
        }

        try {

            customerService.updateCustomer(
                    id,
                    customerForm
            );

        } catch (IllegalArgumentException e) {

            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    e.getMessage()
            );

            model.addAttribute(
                    "customerId",
                    id
            );

            return "admin/customer-edit";
        }

        redirectAttributes.addFlashAttribute(
                "message",
                "顧客を更新しました。ID: " + id
        );

        return "redirect:/admin/customers";
    }

    @PostMapping("/{id}/active")
    public String updateActive(
            @PathVariable Long id,
            @RequestParam Integer active,
            RedirectAttributes redirectAttributes) {

        customerService.updateCustomerActive(
                id,
                active
        );

        String message =
                active == 1
                        ? "顧客を有効化しました。ID: " + id
                        : "顧客を無効化しました。ID: " + id;

        redirectAttributes.addFlashAttribute(
                "message",
                message
        );

        return "redirect:/admin/customers";
    }
}