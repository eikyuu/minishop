// validation/ValidPrice.java — l'annotation custom
package com.minishop.product.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPriceValidator.class)
@Documented
public @interface ValidPrice {

    String message() default "Price must be a valid monetary amount (max 2 decimal places)";

    // Ces deux attributs sont obligatoires pour toute annotation de validation
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}