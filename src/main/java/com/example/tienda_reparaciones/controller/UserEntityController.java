package com.example.tienda_reparaciones.controller;

import com.example.tienda_reparaciones.DTO.UserRegisterDTO;
import com.example.tienda_reparaciones.model.UserEntity;
import com.example.tienda_reparaciones.service.UserEntityService;
import com.example.tienda_reparaciones.utils.PaginationLinksUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.HashMap;
import java.util.Map;

//@PreAuthorize("hasRole('ADMIN')")
@RestController
public class UserEntityController {



    private final UserEntityService userEntityService;


    private final PaginationLinksUtils paginationLinksUtils;

    public UserEntityController(UserEntityService userEntityService, PaginationLinksUtils paginationLinksUtils) {
        this.userEntityService = userEntityService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    @GetMapping("/usuarios")
    public ResponseEntity<Page<UserEntity>> getUsuarios(@PageableDefault(size = 5) Pageable pageable) {

        Page<UserEntity> usuarios = userEntityService.findAllPageable(pageable);


        UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();

        return ResponseEntity.ok().header("link",
                paginationLinksUtils.createLinkHeader(usuarios, uriComponentsBuilder)).body(usuarios);

    }


    @PostMapping("/usuarios/{isAdmin}")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody UserRegisterDTO user, BindingResult bindingResult, @PathVariable Boolean isAdmin){
        if(bindingResult.hasErrors()){
            return error(bindingResult);
        }

        userEntityService.register(user, isAdmin);
        return new ResponseEntity<>(user, HttpStatus.CREATED);     // 201 CREATED
        // return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable Long id){
        return userEntityService.findById(id)
                .map(user -> ResponseEntity.ok().body(user)) //200 OK
                .orElse(ResponseEntity.notFound().build());         // 404 NOT FOUND
    }

//    @PutMapping("/usuarios/{id}")
//    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserEntity nuevoUser, BindingResult bindingResult){
//        if(bindingResult.hasErrors()){
//            return error(bindingResult);
//        }
//        return userEntityService.findById(id)
//                .map(user -> {
//                    user.setUsername(nuevoUser.getUsername());
//                    user.setEmail(nuevoUser.getEmail());
//                    userEntityService.save(user, false);
//                    return ResponseEntity.ok().body(user);  //200  OK
//                }).orElseGet(() -> ResponseEntity.ok().body(nuevoUser));
//
//    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        return userEntityService.findById(id)
                .map(user -> {
                    userEntityService.delete(user);
                    return ResponseEntity.noContent().<Void>build();  // 204 NOT CONTENT
                })
                .orElse(ResponseEntity.notFound().build());  // 404 NOT FOUND
    }

    public ResponseEntity<?> error(BindingResult bindingResult){
        Map<String,String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err ->
            errors.put(err.getField(), "El campo " + err.getField() + " " +err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

}
