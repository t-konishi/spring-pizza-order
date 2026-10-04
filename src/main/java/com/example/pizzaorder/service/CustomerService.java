package com.example.pizzaorder.service;

import com.example.pizzaorder.entity.Customer;
import com.example.pizzaorder.repository.CustomerRepository;
import com.example.pizzaorder.form.CustomerForm;
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

    @Transactional(readOnly = true)
    public List<Customer> findAllCustomers() {
        return customerRepository.findAllByOrderByCustomerIdAsc();
    }

    @Transactional
    public Long createCustomer(CustomerForm form) {

        if (customerRepository.existsByEmailIgnoreCase(
                form.getEmail())) {

            throw new IllegalArgumentException(
                    "このメールアドレスは既に登録されています"
            );
        }

        Customer customer = new Customer();

        customer.setCustomerName(form.getCustomerName());
        customer.setEmail(form.getEmail());
        customer.setPhone(form.getPhone());
        customer.setActive(1);

        Customer savedCustomer =
                customerRepository.save(customer);

        return savedCustomer.getCustomerId();
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findCustomerById(
            Long customerId) {

        return customerRepository.findById(customerId);
    }

    @Transactional
    public void updateCustomer(
            Long customerId,
            CustomerForm form) {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found: "
                                        + customerId
                        )
                );

        if (customerRepository
                .existsByEmailIgnoreCaseAndCustomerIdNot(
                        form.getEmail(),
                        customerId
                )) {

            throw new IllegalArgumentException(
                    "このメールアドレスは既に登録されています"
            );
        }

        customer.setCustomerName(
                form.getCustomerName()
        );

        customer.setEmail(
                form.getEmail()
        );

        customer.setPhone(
                form.getPhone()
        );
    }
}