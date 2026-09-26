package com.example.pizzaorder.dto;

import com.example.pizzaorder.entity.OrderItem;
import com.example.pizzaorder.entity.Payment;

import java.util.List;

public record OrderDetailView(
        OrderDetailDto order,
        List<OrderItem> items,
        List<Payment> payments
) {
}
