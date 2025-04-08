package com.example.tienda_reparaciones.service;



import com.example.tienda_reparaciones.model.Repair;
import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.RepairRepository;
import com.example.tienda_reparaciones.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RepairService {

    @Autowired
    private RepairRepository repairRepository;



    @Autowired
    private UserEntityRepository userEntityRepository;

    public Repair createRepair(Long userId, String marca, String modelo, List<String> piezas, Duration duracion) {
        // Validar cliente y mesa
        UserEntity user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));



        // Crear nueva repair usando el builder
        Repair repair = Repair.builder()
                .user(user)
                .marca(marca)
                .modelo(modelo)
                .piezas(piezas)
                .duracion(duracion)
                .build();

        // Guardar repair en la base de datos
        return repairRepository.save(repair);
    }



}


