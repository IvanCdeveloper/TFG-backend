package com.example.tienda_reparaciones.controller;

import com.example.tienda_reparaciones.DTO.LoginRequestDTO;
import com.example.tienda_reparaciones.DTO.LoginResponseDTO;
import com.example.tienda_reparaciones.DTO.UserRegisterDTO;
import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.service.UserEntityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@RequestMapping("/api")
@RestController
public class AuthController {

    private final UserEntityService userEntityService;


    public AuthController(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;

    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> save(@Valid @RequestBody UserRegisterDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return error(bindingResult);
        }

        return userEntityService.register(userDTO, false);


    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return error(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntityService.login(loginDTO));
    }

    @GetMapping("/auth/check-status")
    public ResponseEntity<?> checkStatus(Authentication auth, HttpServletRequest request) {
        try {
            UserEntity user = (UserEntity) auth.getPrincipal();

            // Opcional: obtener el token directamente del header
            String authHeader = request.getHeader("Authorization");
            String token = (authHeader != null && authHeader.startsWith("Bearer "))
                    ? authHeader.substring(7)
                    : "Token no disponible";

            return ResponseEntity.ok(Map.of("user", Map.of(
                    "email", user.getEmail(),
                    "username", user.getUsername()
            ), "token", token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of(
                            "path", "/auth/check-status",
                            "message", "Credenciales erróneas",
                            "timestamp", new Date()
                    )
            );
        }
    }

    public ResponseEntity<?> error(BindingResult bindingResult){
        Map<String,String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err ->
                errors.put(err.getField(), "El " + err.getField() + " " +err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

}
