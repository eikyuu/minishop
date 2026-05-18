package com.minishop.product.exception;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {

    private final Long productId;

    public ProductNotFoundException(Long productId) {
        super(String.format("No product with id %d", productId));
        this.productId = productId;
    }

}
