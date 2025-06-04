package com.example.tienda_reparaciones.DTO;

import com.example.tienda_reparaciones.validation.ExistsEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que incrustamos en la respuesta a la solicitud de un inicio de sesión
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {

    @ExistsEmail
    @NotBlank
    @Email(message = "no tiene un formato válido")
    private String email;

    @NotBlank
    private String password;
}

/*
 * También podríamos utilizar un record de Java 14:
 * public record DTOLoginRequest(String username, String password) { }
 * Con esta única línea de código, Java automáticamente genera:
 * Constructor
 * Getters (username() y password())
 * equals() y hashCode()
 * toString()
 */