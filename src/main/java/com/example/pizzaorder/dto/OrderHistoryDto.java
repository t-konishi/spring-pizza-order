package com.example.pizzaorder.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderHistoryDto(
        Long orderId,
        String customerName,
        String orderStatus,
        String orderChannel,
        BigDecimal orderTotal,
        LocalDateTime orderedAt
) {
}
