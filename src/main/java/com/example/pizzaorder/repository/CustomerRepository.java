package com.example.pizzaorder.repository;

import com.example.pizzaorder.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    List<Customer> findByActiveOrderByCustomerIdAsc(Integer active);

    Optional<Customer> findByCustomerIdAndActive(
            Long customerId,
            Integer active
    );
}