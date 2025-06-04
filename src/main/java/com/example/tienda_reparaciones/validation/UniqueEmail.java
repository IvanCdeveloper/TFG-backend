package com.example.tienda_reparaciones.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


/**
 * Anotación que implementa la validación UniqueEmailValidator
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "ya está registrado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
