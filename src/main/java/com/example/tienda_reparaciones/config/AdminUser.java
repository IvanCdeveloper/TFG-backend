package com.example.tienda_reparaciones.config;

import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.UserEntityRepository;
import jakarta.validation.ConstraintValidator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Clase que crea dos usuarios de prueba al iniciar la aplicación,
 * uno con rol user y otro con rol admin
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-05-01
 */

@Component
public class AdminUser implements CommandLineRunner{

    private final UserEntityRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AdminUser(UserEntityRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        createUserIfNotExists("admin", "admin@gmail.com", "admin",  List.of("ROLE_ADMIN", "ROLE_USER"));
        createUserIfNotExists("user", "user@gmail.com", "user", List.of("ROLE_USER"));
    }

    private void createUserIfNotExists(String username, String email, String rawPassword, List<String> roles) {
        if (userRepository.findByUsername(username).isEmpty()) {
            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setAuthorities(roles);
            userRepository.save(user);
            System.out.println("Created user: " + username);
        }
    }
}
