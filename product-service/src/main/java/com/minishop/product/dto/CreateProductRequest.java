// dto/CreateProductRequest.java — version complète avec promoPrice
package com.minishop.product.dto;

import com.minishop.product.validation.ValidPrice;
import com.minishop.product.validation.ValidPriceRange;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@ValidPriceRange  // ← annotation de classe, valide la cohérence price/promoPrice
public record CreateProductRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 200, message = "Name must be between 2 and 200 characters")
        String name,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 digits and 2 decimals")
        @ValidPrice
        BigDecimal price,

        // Optionnel : null = pas de promotion
        // Si renseigné, doit être < price (vérifié par @ValidPriceRange)
        @Positive(message = "Promotional price must be greater than zero")
        @ValidPrice
        BigDecimal promoPrice,

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock cannot be negative")
        @Max(value = 10000, message = "Stock cannot exceed 10000")
        Integer stock
) {}