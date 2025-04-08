package com.example.tienda_reparaciones.service;



import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserEntityService {

    @Autowired
    private UserEntityRepository userEntityRepository;
    public Page<UserEntity> findAllPageable(Pageable pageable){
        return userEntityRepository.findAll(pageable);
    }
}