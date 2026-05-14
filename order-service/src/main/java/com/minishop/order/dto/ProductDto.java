package com.minishop.order.dto;
// dto/ProductDto.java  ← copie locale dans order-service

import java.math.BigDecimal;

// order-service a SA propre représentation de ProductDto
// Il ne dépend pas du module product-service — couplage minimal
// Il prend uniquement ce dont il a besoin
public record ProductDto(
        Long id,
        String name,
        BigDecimal price
) {}