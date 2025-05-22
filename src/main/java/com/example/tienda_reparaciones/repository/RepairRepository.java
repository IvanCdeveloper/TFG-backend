package com.example.tienda_reparaciones.repository;


import com.example.tienda_reparaciones.model.Repair;
import com.example.tienda_reparaciones.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {

    Page<Repair> findAllByUserId(Long userId, Pageable pageable);
    Page<Repair> findAllByUser(UserEntity user, Pageable pageable);


    Long id(Long id);
}
