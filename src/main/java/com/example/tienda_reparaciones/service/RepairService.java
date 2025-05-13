package com.example.tienda_reparaciones.service;



import com.example.tienda_reparaciones.model.Repair;
import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.RepairRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
public class RepairService {

    private final RepairRepository repairRepository;

    private final UserEntityService userEntityService;

    public RepairService(RepairRepository repairRepository, UserEntityService userEntityService) {
        this.repairRepository = repairRepository;
        this.userEntityService = userEntityService;
    }

    @Transactional
    public Repair createRepair(Long userId, String marca, String modelo, List<String> piezas, Duration duracion) {
        // Validar cliente y mesa
        UserEntity user = userEntityService.findById(userId)
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

    @Transactional
    public Repair save(Repair repair) {
       return repairRepository.save(repair);
    }

    @Transactional(readOnly = true)
    public Page<Repair> findAllPageable(Pageable pageable){

        return repairRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Repair> findAllByUserId(Long id, Pageable pageable){
        return repairRepository.findAllByUserId(id, pageable);
    }



    public Page<Repair> findAllByUser(UserEntity user, Pageable pageable){

        return repairRepository.findAllByUser(user, pageable);
    }





}


