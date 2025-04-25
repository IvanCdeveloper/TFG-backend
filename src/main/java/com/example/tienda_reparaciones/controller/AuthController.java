package com.example.tienda_reparaciones.controller;

import com.example.tienda_reparaciones.DTO.LoginRequestDTO;
import com.example.tienda_reparaciones.DTO.LoginResponseDTO;
import com.example.tienda_reparaciones.DTO.UserRegisterDTO;
import com.example.tienda_reparaciones.config.JwtTokenProvider;
import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.UserEntityRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private UserEntityRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth/register")
    public ResponseEntity<?> save(@RequestBody UserRegisterDTO userDTO) {

        try {
            UserEntity user = this.userRepository.save(
                    UserEntity.builder()
                            .username(userDTO.getUsername())
                            .password(passwordEncoder.encode(userDTO.getPassword()))
                            .email(userDTO.getEmail())
                            .authorities(List.of("ROLE_USER", "ROLE_ADMIN"))
                            .build());


            //Validamos al usuario en Spring (hacemos login manualmente)
            UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
            Authentication auth = authenticationManager.authenticate(userPassAuthToken);    //valida el usuario y devuelve un objeto Authentication con sus datos
            //Obtenemos el Cliente del usuario logueado


            //Generamos un token con los datos del usuario (la clase tokenProvider ha hemos creado nosotros para no poner aquí todo el código
            String token = this.tokenProvider.generateToken(auth);

            return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponseDTO(user.getEmail(), token));

        }catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }


    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO) {
        try {

            //Validamos al usuario en Spring (hacemos login manualmente)
            UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
            Authentication auth = authenticationManager.authenticate(userPassAuthToken);    //valida el usuario y devuelve un objeto Authentication con sus datos
            //Obtenemos el Cliente del usuario logueado
            UserEntity user = (UserEntity) auth.getPrincipal();

            //Generamos un token con los datos del usuario (la clase tokenProvider ha hemos creado nosotros para no poner aquí todo el código
            String token = this.tokenProvider.generateToken(auth);

            //Devolvemos un código 200 con el username y token JWT
            return ResponseEntity.ok(new LoginResponseDTO(user.getEmail(), token));
        }catch (Exception e) {  //Si el usuario no es válido, salta una excepción BadCredentialsException
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of(
                            "path", "/auth/login",
                            "message", e.getMessage(),
                            "timestamp", new Date()
                    )
            );
        }
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

}
