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

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Repair {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String brand;
    @NotBlank
    private String model;

    @NotEmpty
    @ManyToMany
    @JoinTable(
            name = "repair_part",
            joinColumns = @JoinColumn(name = "repair_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    private List<Part> parts = new ArrayList<>();


    private Duration duration;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Transient
    private BigDecimal price;



    public BigDecimal getPrice() {
        return parts.stream()
                .reduce(
                        BigDecimal.ZERO,
                        (sum, part) -> sum.add(BigDecimal.valueOf(part.getPrice())),
                        BigDecimal::add
                );
    }



}
