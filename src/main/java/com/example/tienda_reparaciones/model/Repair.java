package com.example.tienda_reparaciones.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Repair que es las distintas reparaciónes que pueden solicitar los usuarios.
 * Tiene los atributos marca, modelo, una relación muchos a muchos con parts, la duración
 * que es la diferencia entre el instante actual y la fecha de fin de la reparación, la fecha
 * fin de la reparación, una relación muchos a uno con usuario y el precio que se calcula
 * obteniendo el precio todas las partes que componen la reparación y sumandolas.
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "repairs")
public class Repair {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String brand;
    @NotBlank
    private String model;

    @NotEmpty
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "repair_part",
            joinColumns = @JoinColumn(name = "repair_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    private List<Part> parts = new ArrayList<>();


    @Transient
    public Duration getDuration() {
        if (endTime == null) {
            return Duration.ZERO;      // o lanza, o null, según tu caso
        }
        // Entre ahora y la fecha de fin
        return Duration.between(LocalDateTime.now(), endTime);
    }

    private LocalDateTime endTime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Formula(
            "(SELECT SUM(p.price) " +
                    " FROM repair_part rp " +
                    " JOIN part p ON rp.part_id = p.id " +
                    " WHERE rp.repair_id = id)"
    )
    private BigDecimal price;







}
