package com.example.pizzaorder.service;

import com.example.pizzaorder.entity.OrderItem;
import com.example.pizzaorder.entity.Pizza;
import com.example.pizzaorder.entity.PizzaOrder;
import com.example.pizzaorder.repository.OrderItemRepository;
import com.example.pizzaorder.repository.PizzaOrderRepository;
import com.example.pizzaorder.repository.PizzaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final PizzaRepository pizzaRepository;
    private final PizzaOrderRepository pizzaOrderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(
            PizzaRepository pizzaRepository,
            PizzaOrderRepository pizzaOrderRepository,
            OrderItemRepository orderItemRepository) {

        this.pizzaRepository = pizzaRepository;
        this.pizzaOrderRepository = pizzaOrderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public Long placeOrder(Long pizzaId, Integer quantity) {

        if (quantity == null || quantity < 1 || quantity > 99) {
            throw new IllegalArgumentException(
                    "Quantity must be between 1 and 99"
            );
        }

        Pizza pizza = pizzaRepository
                .findByPizzaIdAndActive(pizzaId, 1)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pizza not found or inactive"
                        )
                );

        BigDecimal subtotal = pizza.getPrice()
                .multiply(BigDecimal.valueOf(quantity));

        PizzaOrder order = new PizzaOrder();
        order.setCustomerId(1L);
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

        return savedOrder.getOrderId();
    }
}