package com.example.tienda_reparaciones.service;



import com.example.tienda_reparaciones.model.Repair;

import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.RepairRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


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


    @Transactional
    public Repair delete(Repair repair) {
        repairRepository.delete(repair);
        return repair;
    }

    @Transactional(readOnly = true)
    public Optional<Repair> findById(Long id) {
        return repairRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Repair> findAllPageable(Pageable pageable){

        return repairRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Repair> findAllByUserId(Long id, Pageable pageable){
        return repairRepository.findAllByUserId(id, pageable);
    }


    @Transactional()
    public Optional<Repair> findRepairIfOwnedByUser(Long repairId, Long userId) {
        return repairRepository.findById(repairId)
                .filter(repair -> repair.getUser().getId().equals(userId));
    }







}


