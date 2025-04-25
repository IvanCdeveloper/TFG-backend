package com.example.tienda_reparaciones.controller;


import com.example.tienda_reparaciones.model.Repair;
import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.RepairRepository;
import com.example.tienda_reparaciones.repository.UserEntityRepository;
import com.example.tienda_reparaciones.service.RepairService;
import com.example.tienda_reparaciones.utils.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;



@RestController
public class RepairController {

    @Autowired
    private RepairRepository repairRepository;

    @Autowired
    private RepairService repairService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private PaginationLinksUtils paginationLinksUtils;

    @GetMapping("/repairs")
    public ResponseEntity<Page<Repair>> getAllRepairs(@PageableDefault(page = 0, size = 6) Pageable pageable, HttpServletRequest request) {
        Page<Repair> repairs = repairService.findAllPageable(pageable);

        UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();

        return ResponseEntity.ok().header("link",
                paginationLinksUtils.createLinkHeader(repairs, uriComponentsBuilder)).body(repairs);

    }

    @PostMapping("/repairs")
    public ResponseEntity<?> createRepair( @RequestBody Repair repair, Authentication authentication) {
        String username = authentication.getName();
        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        repair.setUser(userEntity);

        return ResponseEntity.ok().body(repairRepository.save(repair));
    }
}
