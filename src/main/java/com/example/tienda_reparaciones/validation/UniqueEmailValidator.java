package com.example.tienda_reparaciones.validation;

import com.example.tienda_reparaciones.service.UserEntityService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

/**
 * Validador que verifica si el email que ha introducido no existe en la base de datos
 * para que el usuario se pueda registrar con ese email
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */


@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserEntityService userEntityService;

    UniqueEmailValidator(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }


    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true;
        }

        return !userEntityService.existsByEmail(email);
    }
}
