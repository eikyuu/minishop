package com.minishop.order.dto;

import com.minishop.order.entity.OrderEntity;
import com.minishop.order.entity.OrderStatus;

import java.time.Instant;

public record OrderDto(
        Long id,
        Long productId,
        Integer quantity,
        OrderStatus status,
        Instant createdAt
) {}
