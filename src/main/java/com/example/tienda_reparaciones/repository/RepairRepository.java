package com.example.tienda_reparaciones.repository;


import com.example.tienda_reparaciones.model.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {

}
