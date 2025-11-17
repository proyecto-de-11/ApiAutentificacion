package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "El correo de usuario es requerido")
    private String email;

    @Column(name = "contrasena", nullable = false)
    @NotBlank(message = "La contraseña es requerida")
    private String contrasena;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRol", nullable = false)
    private Rol rol;

    @Column(name = "estaActivo", nullable = false, columnDefinition = "BOOLEAN")
    private Boolean estaActivo;

    @Column(name = "fecha_Creacion", nullable = false, columnDefinition = "DATETIME")
    private String fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = true, columnDefinition = "DATETIME")
    private String fechaActualizacion;

    // ========== IMPLEMENTACIÓN DE UserDetails ==========
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (rol != null) {
            return Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + rol.getNombre().toUpperCase())
            );
        }
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return estaActivo != null && estaActivo;
    }
    // ========== FIN IMPLEMENTACIÓN UserDetails ==========

    // Getters y Setters normales
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getEstaActivo() {
        return estaActivo;
    }

    public void setEstaActivo(Boolean estaActivo) {
        this.estaActivo = estaActivo;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}