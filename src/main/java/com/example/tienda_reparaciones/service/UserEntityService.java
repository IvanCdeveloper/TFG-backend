package com.example.tienda_reparaciones.service;



import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.UserEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserEntityService {

    private final UserEntityRepository userEntityRepository;

    public UserEntityService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    public Page<UserEntity> findAllPageable(Pageable pageable){
        return userEntityRepository.findAll(pageable);
    }

    public Optional<UserEntity> findById(Long id) {
        return userEntityRepository.findById(id);
    }

    public UserEntity save(UserEntity userEntity) {
        return userEntityRepository.save(userEntity);

    }
    public void delete(UserEntity userEntity) {
        userEntityRepository.delete(userEntity);
    }
}