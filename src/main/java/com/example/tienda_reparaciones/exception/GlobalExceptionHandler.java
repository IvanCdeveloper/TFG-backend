package com.example.tienda_reparaciones.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * Manejador de excepciones que se encarga de lanzar distintas excepciones
 * En este caso solo tiene la PasswordsDoNotMatchException y devuelve una
 * respuesta con el campo en el que se ha producido el error y el mensaje del error
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public ResponseEntity<?> handlePasswordsDoNotMatchException(PasswordsDoNotMatchException ex) {
        return ResponseEntity.badRequest().body(Map.of("password2", ex.getMessage()));
    }


}
