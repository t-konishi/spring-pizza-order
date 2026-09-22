package com.example.pizzaorder.service;

import com.example.pizzaorder.entity.Customer;
import com.example.pizzaorder.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(
            CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<Customer> findActiveCustomers() {
        return customerRepository
                .findByActiveOrderByCustomerIdAsc(1);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findActiveCustomerById(
            Long customerId) {

        return customerRepository
                .findByCustomerIdAndActive(customerId, 1);
    }
}