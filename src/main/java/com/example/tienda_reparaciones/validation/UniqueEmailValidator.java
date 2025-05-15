package com.example.tienda_reparaciones.validation;

import com.example.tienda_reparaciones.service.UserEntityService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;


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
