package com.example.tienda_reparaciones.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;




/**
 * Anotación que implementa la validación EmailExistsValidator
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */

@Documented
@Constraint(validatedBy = EmailExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsEmail {
    String message() default "no esta registrado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
