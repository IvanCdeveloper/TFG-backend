package com.example.tienda_reparaciones.controller;


import com.example.tienda_reparaciones.model.Part;
import com.example.tienda_reparaciones.model.Repair;
import com.example.tienda_reparaciones.model.UserEntity;

import com.example.tienda_reparaciones.service.PartService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
public class RepairController {


    private final RepairService repairService;
    private final PaginationLinksUtils paginationLinksUtils;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PartService partService;

    public RepairController(RepairService repairService, PaginationLinksUtils paginationLinksUtils, UserDetailsServiceImpl userDetailsServiceImpl, PartService partService) {

        this.repairService = repairService;
        this.paginationLinksUtils = paginationLinksUtils;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.partService = partService;
    }

    @GetMapping("/repairs")
    public ResponseEntity<Page<Repair>> getUserRepairs(@PageableDefault(size = 6) Pageable pageable, Authentication authentication) {

        UserEntity user= (UserEntity) authentication.getPrincipal();

        Page<Repair> repairs = repairService.findAllByUserId( user.getId(), pageable);

        UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();

        return ResponseEntity.ok().header("link",
                paginationLinksUtils.createLinkHeader(repairs, uriComponentsBuilder)).body(repairs);

    }

    @PostMapping("/repairs")
    public ResponseEntity<?> createRepair(@Valid @RequestBody Repair repair, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return error(bindingResult);
        }

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        UserEntity user = userDetailsServiceImpl.loadUserByUsername(userEntity.getEmail());
        repair.setUser(user);

        // Recuperar las partes completas desde sus IDs
        List<Part> fullParts = repair.getParts().stream()
                .map(part -> partService.findById(part.getId())) // asegúrate que existe
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        repair.setParts(fullParts);

        Repair savedRepair = repairService.save(repair);

        return ResponseEntity.ok(savedRepair);
    }

    public ResponseEntity<?> error(BindingResult bindingResult){
        Map<String,String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err ->
            errors.put(err.getField(), "El campo " + err.getField() + " " +err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
