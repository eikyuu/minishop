// validation/ValidPriceRange.java
package com.minishop.product.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

// Target = TYPE : s'applique sur la classe entière, pas un champ
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPriceRangeValidator.class)
@Documented
public @interface ValidPriceRange {
    String message() default "Promotional price must be less than regular price";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}