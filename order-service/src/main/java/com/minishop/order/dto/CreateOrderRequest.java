package com.minishop.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(
        @NotNull Long productId,
        @NotNull @Min(1) Integer quantity
) {
}
