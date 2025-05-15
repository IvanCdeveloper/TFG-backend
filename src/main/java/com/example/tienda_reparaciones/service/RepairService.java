package com.example.tienda_reparaciones.service;



import com.example.tienda_reparaciones.model.Repair;

import com.example.tienda_reparaciones.repository.RepairRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class RepairService {

    private final RepairRepository repairRepository;



    public RepairService(RepairRepository repairRepository) {
        this.repairRepository = repairRepository;
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









}


