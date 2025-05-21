package com.example.tienda_reparaciones.controller;


import com.example.tienda_reparaciones.model.Part;
import com.example.tienda_reparaciones.service.PartService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("parts")
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
