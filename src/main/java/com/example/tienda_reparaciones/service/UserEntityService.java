package com.example.tienda_reparaciones.service;



import com.example.tienda_reparaciones.DTO.LoginRequestDTO;
import com.example.tienda_reparaciones.DTO.LoginResponseDTO;
import com.example.tienda_reparaciones.DTO.UserRegisterDTO;
import com.example.tienda_reparaciones.config.JwtTokenProvider;
import com.example.tienda_reparaciones.exception.PasswordsDoNotMatchException;
import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.UserEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Servicio para registrar, loguear y eliminar usuarios, además de obtener los permisos, el token de los usuarios.
 * Tambien para comprobar si el email escrito por el usuario ya existe en base de datos
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 *
 */
@Service
public class UserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public UserEntityService(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> findAllPageable(Pageable pageable){
        return userEntityRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findById(Long id) {
        return userEntityRepository.findById(id);
    }

    @Transactional()
    public ResponseEntity<?> register(UserRegisterDTO dto, Boolean isAdmin) {
        if(!Objects.equals(dto.getPassword(), dto.getPassword2())){
            throw new PasswordsDoNotMatchException();
        }

        try{

            UserEntity user;
            if(isAdmin){
                user = UserEntity.builder()
                        .username(dto.getUsername())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .email(dto.getEmail())
                        .authorities(List.of("ROLE_USER", "ROLE_ADMIN"))
                        .build();

            }else{
                user = UserEntity.builder()
                    .username(dto.getUsername())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .email(dto.getEmail())
                    .authorities(List.of("ROLE_USER"))
                    .build();

            }



            user.setAdmin(isAdmin);
            userEntityRepository.save(user);

            String token = authenticateAndGetToken(dto.getEmail(), dto.getPassword());




            return ResponseEntity.ok(Map.of("user", Map.of(
                    "email", user.getEmail(),
                    "username", user.getUsername(),
                    "roles", user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList())
            ), "token", token));
        }catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }



    }
    public LoginResponseDTO login(LoginRequestDTO dto) {
        String token = authenticateAndGetToken(dto.getEmail(), dto.getPassword());
        Authentication auth = getAuthentication(dto.getEmail(), dto.getPassword());
        UserEntity user = (UserEntity) auth.getPrincipal();
        return new LoginResponseDTO(
                Map.of("email", user.getEmail(), "username", user.getUsername(), "roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())),
                token
        );
    }

    @Transactional()
    public void delete(UserEntity userEntity) {
        userEntityRepository.delete(userEntity);
    }

    private Authentication getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(authToken);
    }

    private String authenticateAndGetToken(String email, String password) {
        Authentication auth = getAuthentication(email, password);
        return tokenProvider.generateToken(auth);
    }

    public boolean existsByEmail(String email) {
        return userEntityRepository.existsByEmail(email);
    }
}