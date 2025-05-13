package com.example.tienda_reparaciones.service;

import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.UserEntityRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserEntityServiceImpl implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    UserEntityServiceImpl(UserEntityRepository userEntityRepository) {
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
