package com.example.pizzaorder.service;

import com.example.pizzaorder.entity.PizzaOrder;
import com.example.pizzaorder.repository.CustomerRepository;
import com.example.pizzaorder.repository.OrderItemRepository;
import com.example.pizzaorder.repository.PaymentRepository;
import com.example.pizzaorder.repository.PizzaOrderRepository;
import com.example.pizzaorder.repository.PizzaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private PizzaRepository pizzaRepository;

    @Mock
    private PizzaOrderRepository pizzaOrderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void preparingToReadyIsAllowed() {

        PizzaOrder order = new PizzaOrder();
        order.setOrderId(21L);
        order.setOrderStatus("PREPARING");

        when(pizzaOrderRepository.findById(21L))
                .thenReturn(Optional.of(order));

        orderService.updateOrderStatus(
                21L,
                "READY"
        );

        assertEquals(
                "READY",
                order.getOrderStatus()
        );
    }

    @Test
    void readyToReceivedIsRejected() {

        PizzaOrder order = new PizzaOrder();
        order.setOrderId(21L);
        order.setOrderStatus("READY");

        when(pizzaOrderRepository.findById(21L))
                .thenReturn(Optional.of(order));

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> orderService.updateOrderStatus(
                                21L,
                                "RECEIVED"
                        )
                );

        assertEquals(
                "READY から RECEIVED へは変更できません",
                exception.getMessage()
        );

        assertEquals(
                "READY",
                order.getOrderStatus()
        );
    }
}
