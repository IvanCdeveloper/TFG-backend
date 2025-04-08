package com.example.tienda_reparaciones.controller;

import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.repository.UserEntityRepository;
import com.example.tienda_reparaciones.service.UserEntityService;
import com.example.tienda_reparaciones.utils.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class UserEntityController {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private UserEntityService userEntityService;


    @Autowired
    private PaginationLinksUtils paginationLinksUtils;

    @GetMapping("/usuarios")
    public ResponseEntity<Page<UserEntity>> getUsuarios(@PageableDefault(page = 0, size = 5) Pageable pageable, HttpServletRequest request) {

        Page<UserEntity> usuarios = userEntityService.findAllPageable(pageable);


        UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();

        return ResponseEntity.ok().header("link",
                paginationLinksUtils.createLinkHeader(usuarios, uriComponentsBuilder)).body(usuarios);

    }


//    @PostMapping("/usuarios")
//    public ResponseEntity<UserEntity> crearUsuario(@RequestBody UserEntity user){
//        var user = UserEntityRepository.save(user);
//        return new ResponseEntity<>(user, HttpStatus.CREATED);     // 201 CREATED
//        // return ResponseEntity.status(HttpStatus.CREATED).body(user);
//    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable Long id){
        return userEntityRepository.findById(id)
                .map(user -> ResponseEntity.ok().body(user)) //200 OK
                .orElse(ResponseEntity.notFound().build());         // 404 NOT FOUND
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserEntity nuevoUser){
        return userEntityRepository.findById(id)
                .map(user -> {
                    user.setUsername(nuevoUser.getUsername());
                    user.setEmail(nuevoUser.getEmail());
                    userEntityRepository.save(user);
                    return ResponseEntity.ok().body(user);  //200  OK
                }).orElseGet(() -> {
                    return ResponseEntity.ok().body(nuevoUser);
                });

    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        return userEntityRepository.findById(id)
                .map(user -> {
                    userEntityRepository.delete(user);
                    return ResponseEntity.noContent().<Void>build();  // 204 NOT CONTENT
                })
                .orElse(ResponseEntity.notFound().build());  // 404 NOT FOUND
    }

}
