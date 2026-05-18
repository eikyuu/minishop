// validation/ValidPriceRangeValidator.java
package com.minishop.product.validation;

import com.minishop.product.dto.CreateProductRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPriceRangeValidator
        implements ConstraintValidator<ValidPriceRange, CreateProductRequest> {

    @Override
    public boolean isValid(CreateProductRequest request,
                           ConstraintValidatorContext context) {
        if (request.price() == null || request.promoPrice() == null) return true;

        boolean valid = request.promoPrice()
                .compareTo(request.price()) < 0;

        if (!valid) {
            // Personnaliser le champ associé au message d'erreur
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Promotional price must be less than regular price")
                    .addPropertyNode("promoPrice")
                    .addConstraintViolation();
        }

        return valid;
    }
}