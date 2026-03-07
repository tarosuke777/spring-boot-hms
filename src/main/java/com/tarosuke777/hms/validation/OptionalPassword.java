package com.tarosuke777.hms.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OptionalPasswordValidator.class)
public @interface OptionalPassword {
    String message() default "is single-byte alphanumeric characters (4-20 chars)";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
