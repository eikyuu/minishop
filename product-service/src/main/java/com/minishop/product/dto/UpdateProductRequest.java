package com.minishop.product.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

// Sur un PATCH, tous les champs sont optionnels
// On valide uniquement si la valeur est présente
public record UpdateProductRequest(

        // @Size s'applique uniquement si name != null
        @Size(min = 2, max = 200, message = "Name must be between 2 and 200 characters")
        String name,

        // @Positive s'applique uniquement si price != null
        @Positive(message = "Price must be greater than zero")
        BigDecimal price
) {}