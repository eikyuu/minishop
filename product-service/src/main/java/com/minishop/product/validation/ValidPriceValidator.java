// validation/ValidPriceValidator.java — la logique de validation
package com.minishop.product.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class ValidPriceValidator
        implements ConstraintValidator<ValidPrice, BigDecimal> {

    @Override
    public boolean isValid(BigDecimal value,
                           ConstraintValidatorContext context) {
        // null est géré par @NotNull — un validateur custom
        // ne doit pas re-valider null (principe de responsabilité unique)
        if (value == null) return true;

        // scale() = nombre de décimales : 9.999 → scale = 3 → invalide
        return value.scale() <= 2;
    }
}