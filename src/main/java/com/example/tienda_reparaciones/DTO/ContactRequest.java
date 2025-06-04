package com.example.tienda_reparaciones.DTO;

import lombok.Data;

/**
 * DTO que recibimos del frontend cuando recibimos un email
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */

@Data
public class ContactRequest {
    private String name;
    private String email;
    private String subject;
    private String message;
}
