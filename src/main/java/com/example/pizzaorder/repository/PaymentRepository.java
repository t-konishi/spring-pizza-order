package com.example.pizzaorder.repository;

import com.example.pizzaorder.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {
}