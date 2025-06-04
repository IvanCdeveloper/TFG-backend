package com.example.tienda_reparaciones.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



/**
 * Entidad part que es una parte de una reparación como puede ser una pantalla, una bateria,
 * un altavoz, un puerto de carga o una cámara. Se compone del nombre que es el tipo de componente,
 * la marca, el modelo, el precio y una relación muchos a muchos con repairs, ya que puede haber
 * dos reparaciones destintas del mismo modelo de movil y del mismo componente.
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "parts")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String brand;

    private String model;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @JsonIgnore
    @ManyToMany(mappedBy = "parts")
    private List<Repair> repairs = new ArrayList<>();


}
