package com.example.tienda_reparaciones.exception;

public class PasswordsDoNotMatchException extends RuntimeException {
    public PasswordsDoNotMatchException() {
        super("Las contraseñas no coinciden");
    }
}
