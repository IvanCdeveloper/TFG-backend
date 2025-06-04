package com.example.tienda_reparaciones.service;

import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.UserEntityRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio para obtener el usuario a partir de email
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    UserDetailsServiceImpl(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public UserEntity loadUserByUsername(String email) throws UsernameNotFoundException {
        //Extrae el usuario de la BD
        return this.userEntityRepository.findByEmail(email).orElseThrow(
                ()-> new UsernameNotFoundException(email+" no encontrado")
        );

    }
}
