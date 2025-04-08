package com.example.tienda_reparaciones.controller;


import com.example.tienda_reparaciones.model.Repair;
import com.example.tienda_reparaciones.repository.RepairRepository;
import com.example.tienda_reparaciones.repository.UserEntityRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;

@RestController
public class RepairController {

    @Autowired
    private RepairRepository repairRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @GetMapping("/repairs")
    public ResponseEntity<Page<Repair>> getAllRepairs(@PageableDefault(page = 0, size = 6) Pageable pageable, HttpServletRequest request) {
        Page<Repair> repairs = repairRepository.findAllPageable(pageable);
    }
}
