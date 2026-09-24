package com.example.pizzaorder.service;

import com.example.pizzaorder.entity.OrderItem;
import com.example.pizzaorder.entity.Pizza;
import com.example.pizzaorder.entity.PizzaOrder;
import com.example.pizzaorder.repository.OrderItemRepository;
import com.example.pizzaorder.repository.PizzaOrderRepository;
import com.example.pizzaorder.repository.PizzaRepository;
import com.example.pizzaorder.entity.Customer;
import com.example.pizzaorder.repository.CustomerRepository;
import com.example.pizzaorder.entity.Payment;
import com.example.pizzaorder.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final PizzaRepository pizzaRepository;
    private final PizzaOrderRepository pizzaOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    public OrderService(
            PizzaRepository pizzaRepository,
            PizzaOrderRepository pizzaOrderRepository,
            OrderItemRepository orderItemRepository,
            CustomerRepository customerRepository,
            PaymentRepository paymentRepository) {

        this.pizzaRepository = pizzaRepository;
        this.pizzaOrderRepository = pizzaOrderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Long placeOrder(
            Long customerId,
            Long pizzaId,
            Integer quantity,
            String paymentMethod) {

        if (quantity == null || quantity < 1 || quantity > 99) {
            throw new IllegalArgumentException(
                    "Quantity must be between 1 and 99"
            );
        }

        if (!"CREDIT_CARD".equals(paymentMethod)
                && !"CASH".equals(paymentMethod)
                && !"QR".equals(paymentMethod)
                && !"BANK_TRANSFER".equals(paymentMethod)) {

            throw new IllegalArgumentException(
                    "Invalid payment method"
            );
        }

        Pizza pizza = pizzaRepository
                .findByPizzaIdAndActive(pizzaId, 1)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pizza not found or inactive"
                        )
                );

        Customer customer = customerRepository
                .findByCustomerIdAndActive(customerId, 1)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found or inactive"
                        )
                );

        BigDecimal subtotal = pizza.getPrice()
                .multiply(BigDecimal.valueOf(quantity));

        PizzaOrder order = new PizzaOrder();
        order.setCustomerId(customer.getCustomerId());
        order.setOrderStatus("RECEIVED");
        order.setOrderChannel("WEB");
        order.setOrderTotal(subtotal);

        PizzaOrder savedOrder =
                pizzaOrderRepository.save(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(savedOrder.getOrderId());
        orderItem.setPizzaId(pizza.getPizzaId());
        orderItem.setPizzaName(pizza.getPizzaName());
        orderItem.setUnitPrice(pizza.getPrice());
        orderItem.setQuantity(quantity);
        orderItem.setSubtotal(subtotal);

        orderItemRepository.save(orderItem);

        Payment payment = new Payment();
        payment.setOrderId(savedOrder.getOrderId());
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus("PENDING");
        payment.setAmount(subtotal);

        paymentRepository.save(payment);

        return savedOrder.getOrderId();
    }

    @Transactional(readOnly = true)
    public List<PizzaOrder> findOrderHistory() {
        return pizzaOrderRepository
                .findAllByOrderByOrderIdDesc();
    }
}