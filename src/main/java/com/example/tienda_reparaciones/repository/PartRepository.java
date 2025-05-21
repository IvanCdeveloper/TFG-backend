package com.example.tienda_reparaciones.repository;

import com.example.tienda_reparaciones.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

    @Query(" SELECT DISTINCT(p.brand) FROM Part p")
    List<String> findAllBrands();

    @Query(" SELECT DISTINCT(p.model) FROM Part p WHERE p.brand = ?1")
    List<String> findAllModels(String brand);

    @Query(" SELECT p FROM Part p WHERE p.brand = ?1 AND p.model = ?2")
    List<Part> findAllParts(String brand, String model);


}
