package com.example.pizzaorder.controller;

import com.example.pizzaorder.entity.Pizza;
import com.example.pizzaorder.form.OrderForm;
import com.example.pizzaorder.service.PizzaService;
import com.example.pizzaorder.service.OrderService;
import com.example.pizzaorder.entity.Customer;
import com.example.pizzaorder.service.CustomerService;
import com.example.pizzaorder.entity.PizzaOrder;
import com.example.pizzaorder.dto.OrderHistoryDto;
import com.example.pizzaorder.dto.OrderDetailView;
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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.math.BigDecimal;

@Controller
public class OrderController {

    private final PizzaService pizzaService;
    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderController(
            PizzaService pizzaService,
            OrderService orderService,
            CustomerService customerService) {

        this.pizzaService = pizzaService;
        this.orderService = orderService;
        this.customerService = customerService;
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

        List<Customer> customers =
                customerService.findActiveCustomers();

        OrderForm orderForm = new OrderForm();
        orderForm.setPizzaId(pizzaId);
        orderForm.setQuantity(1);

        model.addAttribute("pizza", pizza);
        model.addAttribute("customers", customers);
        model.addAttribute("orderForm", orderForm);

        return "order-form";
    }

    @PostMapping("/orders/confirm")
    public String confirmOrder(
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
            model.addAttribute(
                    "customers",
                    customerService.findActiveCustomers()
            );

            return "order-form";
        }

        Customer customer = customerService
                .findActiveCustomerById(orderForm.getCustomerId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Customer not found or inactive"
                        )
                );

        Integer quantity = orderForm.getQuantity();

        BigDecimal total = pizza.getPrice()
                .multiply(BigDecimal.valueOf(quantity));

        model.addAttribute("quantity", quantity);
        model.addAttribute("total", total);
        model.addAttribute("customer", customer);

        return "order-confirm";
    }

    @PostMapping("/orders")
    public String placeOrder(
            @Valid @ModelAttribute("orderForm") OrderForm orderForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid order"
            );
        }

        Long orderId = orderService.placeOrder(
                orderForm.getCustomerId(),
                orderForm.getPizzaId(),
                orderForm.getQuantity(),
                orderForm.getPaymentMethod()
        );

        model.addAttribute("orderId", orderId);

        return "order-complete";
    }

    @GetMapping("/orders")
    public String orderHistory(Model model) {

        List<OrderHistoryDto> orders =
                orderService.findOrderHistory();

        model.addAttribute("orders", orders);

        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(
            @PathVariable Long id,
            Model model) {

        OrderDetailView detail =
                orderService.findOrderDetail(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Order not found"
                                )
                        );

        model.addAttribute("order", detail.order());
        model.addAttribute("items", detail.items());
        model.addAttribute("payments", detail.payments());

        return "order-detail";
    }
}