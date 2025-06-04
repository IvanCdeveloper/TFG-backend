package com.example.tienda_reparaciones.controller;

import com.example.tienda_reparaciones.DTO.ContactRequest;
import com.example.tienda_reparaciones.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador que recibe el correo del frontend y lo envia a la dirección de correo de la entidad
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */
@RestController
@RequestMapping("/api/contact")

public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> sendContact(@RequestBody ContactRequest req) {
        String body = String.format(
                "Nombre: %s%nEmail: %s%nAsunto: %s%nMensaje:%n%s",
                req.getName(), req.getEmail(), req.getSubject(), req.getMessage()
        );
        // Envía el correo, por ejemplo, a tu propia cuenta
        emailService.sendSimpleMessage("ivaneldioscoc@gmail.com", req.getSubject(), body);
        return ResponseEntity.ok().build();
    }


}
