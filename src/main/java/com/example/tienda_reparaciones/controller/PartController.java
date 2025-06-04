package com.example.tienda_reparaciones.controller;


import com.example.tienda_reparaciones.model.Part;
import com.example.tienda_reparaciones.service.PartService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * Controlador para obtener todos los datos de la tabla parts en la
 * que tenemos almacenados las distintas partes que puedes ser reparadas
 * de diferentes marcas y modelos de marcas con su precio
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */

@RestController
@RequestMapping("/api/parts")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping("/brands")
    public List<String> getBrands() {
        return partService.findAllMarcas();
    }

    @GetMapping("/models")
    public List<String> getModels(@RequestParam String brand) {

        return partService.findAllModelos(brand);
    }

    @GetMapping()
    public List<Part> getParts(
            @RequestParam String brand,
            @RequestParam String model
    ) {
        return partService.findAllParts(brand, model);
    }
}
