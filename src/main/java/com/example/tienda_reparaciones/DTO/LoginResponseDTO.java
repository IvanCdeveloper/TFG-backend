package com.example.tienda_reparaciones.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


/**
 * DTO con los datos que recibimos del cliente cuando inicia sesion
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private Map<String, Object> user;
    private String token;
}