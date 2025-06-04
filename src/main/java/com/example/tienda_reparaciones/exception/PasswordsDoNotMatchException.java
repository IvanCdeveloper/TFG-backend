package com.example.tienda_reparaciones.exception;

/**
 * Excepción en tiempo de ejecución con el mensaje "Las contraseñas no coinciden"
 * esta excepción la maneja la clase GlobalExceptionHandler
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */

public class PasswordsDoNotMatchException extends RuntimeException {
    public PasswordsDoNotMatchException() {
        super("Las contraseñas no coinciden");
    }
}
