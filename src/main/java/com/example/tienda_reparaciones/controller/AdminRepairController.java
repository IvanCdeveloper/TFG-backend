package com.example.tienda_reparaciones.controller;


import com.example.tienda_reparaciones.model.Repair;
import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.RepairRepository;
import com.example.tienda_reparaciones.service.RepairService;
import com.example.tienda_reparaciones.service.UserDetailsServiceImpl;
import com.example.tienda_reparaciones.utils.PaginationLinksUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/admin")
public class AdminRepairController {

    private final RepairRepository repairRepository;

    private final RepairService repairService;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final PaginationLinksUtils paginationLinksUtils;

    public AdminRepairController(RepairRepository repairRepository, RepairService repairService, UserDetailsServiceImpl userDetailsServiceImpl, PaginationLinksUtils paginationLinksUtils) {
        this.repairRepository = repairRepository;
        this.repairService = repairService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    @GetMapping("/repairs")
    public ResponseEntity<Page<Repair>> getAllRepairs(@PageableDefault(size = 6) Pageable pageable) {
        Page<Repair> repairs = repairService.findAllPageable(pageable);

        UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();

        return ResponseEntity.ok().header("link",
                paginationLinksUtils.createLinkHeader(repairs, uriComponentsBuilder)).body(repairs);

    }

    @PostMapping("/repairs")
    public ResponseEntity<?> createRepair(@Valid @RequestBody Repair repair, BindingResult bindingResult ,Authentication authentication) {
        if(bindingResult.hasErrors()){
            return error(bindingResult);
        }
        UserEntity userEntity= (UserEntity) authentication.getPrincipal();
        UserEntity user = userDetailsServiceImpl.loadUserByUsername(userEntity.getEmail());
        repair.setUser(user);
        repairService.save(repair);


        return ResponseEntity.ok().body(repairRepository.save(repair));
    }

    public ResponseEntity<?> error(BindingResult bindingResult){
        Map<String,String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err ->
            errors.put(err.getField(), "El campo " + err.getField() + " " +err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
