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
) {
    public static OrderDto fromEntity(OrderEntity entity) {
        return new OrderDto(
                entity.getId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
