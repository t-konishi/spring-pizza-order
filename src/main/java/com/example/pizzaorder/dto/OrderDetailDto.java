package com.example.pizzaorder.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDetailDto(
        Long orderId,
        Long customerId,
        String customerName,
        String customerEmail,
        String orderStatus,
        String orderChannel,
        BigDecimal orderTotal,
        LocalDateTime orderedAt
) {
}
