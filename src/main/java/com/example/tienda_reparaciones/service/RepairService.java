package com.example.tienda_reparaciones.service;



import com.example.tienda_reparaciones.model.Repair;

import com.example.tienda_reparaciones.repository.RepairRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;



/**
 * Servicio para gestión de reparaciones de dispositivos móviles.
 *
 * Proporciona funcionalidades para crear, consultar, actualizar y eliminar
 * reparaciones, así como calcular precios automáticamente basándose en
 * los componentes seleccionados.
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */
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
    public void delete(Repair repair) {
        repairRepository.delete(repair);
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
    public Page<Repair> findAllPageableSorted(Pageable pageable, String filter){



        return repairRepository.findAllByBrand(filter, pageable );
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


