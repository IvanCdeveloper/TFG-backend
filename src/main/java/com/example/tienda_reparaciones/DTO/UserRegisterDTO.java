package com.example.tienda_reparaciones.DTO;

import com.example.tienda_reparaciones.validation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO con los datos que recibimos del frontend cuando envia un formulario de registro
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDTO {


    @NotBlank
    private String username;


    @UniqueEmail
    @Email(message = "no tiene un formato válido")
    private String email;

    @NotBlank
    @Size(min = 4, max = 32)
    private String password;

    @NotBlank
    @Size(min = 4, max = 32)
    private String password2;

}
