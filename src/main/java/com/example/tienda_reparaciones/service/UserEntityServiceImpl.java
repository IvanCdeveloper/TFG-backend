package com.example.tienda_reparaciones.service;

import com.example.tienda_reparaciones.repository.UserEntityRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserEntityServiceImpl implements UserDetailsService {

    private UserEntityRepository userEntityRepository;

    UserEntityServiceImpl(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //Extrae el usuario de la BD
        return this.userEntityRepository.findByEmail(email).orElseThrow(
                ()-> new UsernameNotFoundException(email+" no encontrado")
        );

    }
}
