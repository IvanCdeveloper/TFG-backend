package com.example.tienda_reparaciones.service;


import com.example.tienda_reparaciones.model.Part;
import com.example.tienda_reparaciones.repository.PartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para encontrar todos las marcas, modelos y partes.
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */

@Service
public class PartService {

    private final PartRepository partRepository;


    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Transactional(readOnly = true)
    public List<Part> findAllParts(String marca, String modelo) {
        return partRepository.findAllParts(marca, modelo);
    }

    @Transactional(readOnly = true)
    public List<String> findAllModelos(String marca){
        return partRepository.findAllModels(marca);
    }

    @Transactional(readOnly = true)
    public List<String> findAllMarcas(){
        return partRepository.findAllBrands();
    }

    public Optional<Part> findById(Long id){
        return partRepository.findById(id);
    };



}
