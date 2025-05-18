package com.example.tienda_reparaciones.service;


import com.example.tienda_reparaciones.repository.PartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PartService {

    private final PartRepository partRepository;


    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Transactional(readOnly = true)
    public List<String> findAllParts(String marca, String modelo) {
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



}
