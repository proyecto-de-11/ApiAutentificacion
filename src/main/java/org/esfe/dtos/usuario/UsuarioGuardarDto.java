package org.esfe.dtos.usuario;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioGuardarDto implements Serializable {

    @NotNull(message = "El correo no puede ser nulo.")
    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El correo debe ser válido.")
    @Size(max = 255, message = "El correo no puede exceder los 255 caracteres.")
    private String email;

    @NotNull(message = "La contraseña no puede ser nula.")
    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres.")
    private String contrasena;

    @NotNull(message = "El id del rol es obligatorio.")
    private Long idRol;

    @NotNull(message = "El estado (estaActivo) es obligatorio.")
    private Boolean estaActivo;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public Boolean getEstaActivo() {
        return estaActivo;
    }

    public void setEstaActivo(Boolean estaActivo) {
        this.estaActivo = estaActivo;
    }
}

