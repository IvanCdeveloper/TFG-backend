package com.example.tienda_reparaciones.validation;

import com.example.tienda_reparaciones.service.UserEntityService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

/**
 * Validador que verifica que el email introducido por el usuario exita en base de datos
 * para que en el caso de que no exista el usuario que haga login sepa que el email introducido
 * no existe en base de datos y que se cree una cuenta
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */

@Component
public class EmailExistsValidator implements ConstraintValidator<ExistsEmail, String> {

    private final UserEntityService userEntityService;

    EmailExistsValidator(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }


    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return false;
        }

        return userEntityService.existsByEmail(email);
    }
}
