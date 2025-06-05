package com.example.tienda_reparaciones.controller;


import com.example.tienda_reparaciones.model.Repair;
import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.RepairRepository;
import com.example.tienda_reparaciones.service.RepairService;
import com.example.tienda_reparaciones.service.UserDetailsServiceImpl;
import com.example.tienda_reparaciones.utils.PaginationLinksUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador que obtiene todas las reparaciones con distintos filtros y ordenación
 * según el administrador eliga en el panel de administración. También puede crear reparaciones
 * no vinculadas a ningun usuario ya que puede haber reparaciones en la tienda de personas que no
 * quieran darse de alta en la página web.
 *
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */


@RestController
@RequestMapping("/api/admin")
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
    public ResponseEntity<Page<Repair>> getAllRepairs(@PageableDefault(size = 10) Pageable pageable) {
        Page<Repair> repairs = repairRepository.findAll(pageable);

        UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();

        return ResponseEntity.ok().header("link",
                paginationLinksUtils.createLinkHeader(repairs, uriComponentsBuilder)).body(repairs);

    }

    @GetMapping("/repairs/sorted")
    public ResponseEntity<Page<Repair>> getAllRepairsSorted(@RequestParam String filter, @RequestParam(required = true) String sortField) {

        Pageable pageable = PageRequest.of(0, 10, Sort.by(sortField).ascending());


        Page<Repair> repairs = repairService.findAllPageableSorted(pageable, filter);

        UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        if(filter == null || filter.isEmpty()) {
            repairs = repairService.findAllPageable(pageable);


        }



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

    @PutMapping("/repairs/{id}")
    public ResponseEntity<?> updateRepair(@PathVariable Long id, @Valid @RequestBody Repair repair, BindingResult bindingResult ,Authentication authentication) {
        if(bindingResult.hasErrors()){
            return error(bindingResult);
        }
        UserEntity userEntity= (UserEntity) authentication.getPrincipal();
        UserEntity user = userDetailsServiceImpl.loadUserByUsername(userEntity.getEmail());
        Repair repairDb = repairService.findById(id)
                        .map(rep ->{
                            rep.setBrand(repair.getBrand());
                            rep.setModel(repair.getModel());
                            rep.setPrice(repair.getPrice());
                            rep.setParts(repair.getParts());
                            rep.setEndTime(repair.getEndTime());
                            return repairRepository.save(rep);
                        }).orElseGet(() -> repairService.save(repair));





        return ResponseEntity.ok().body(repairDb);
    }

    @DeleteMapping("/repairs/{id}")
    public ResponseEntity<?> deleteRepair(@PathVariable Long id, Authentication authentication) {


        Optional<Repair> repairOptional = repairService.findById(id);

        if (repairOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Reparación con id " + id + " no encontrada");
        }

        Repair repair = repairOptional.get();



        repairService.delete(repair);
        return ResponseEntity.ok("Reparación eliminada correctamente");
    }

    public ResponseEntity<?> error(BindingResult bindingResult){
        Map<String,String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err ->
            errors.put(err.getField(), "El campo " + err.getField() + " " +err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
