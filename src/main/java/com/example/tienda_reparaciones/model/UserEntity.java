package com.example.tienda_reparaciones.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Entidad UserEntity que son los usuarios de la aplicación.
 * Tienen un id, un email unico, una contraseña de mínimo 4 carácteres, una propiedad isAdmin, los permisos authorities,
 * una relación uno a muchos con reparaciones y métodos que hereda de UserDetails
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "user_entities")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 4)
    private String password;

    @Transient
    private boolean isAdmin;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    private List<String> authorities;




    @OneToMany(targetEntity = Repair.class, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Repair> repairs = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }




}
