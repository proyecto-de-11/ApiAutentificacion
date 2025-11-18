package org.esfe.dtos.seguridad;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioRegistrar {
    @Email(message = "El correo debe ser válido")
    @NotBlank(message = "El correo es requerido")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    @NotNull(message = "El id del rol es obligatorio")
    private Long idRol;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estaActivo;

    public UsuarioRegistrar() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Long getIdRol() { return idRol; }
    public void setIdRol(Long idRol) { this.idRol = idRol; }
    public Boolean getEstaActivo() { return estaActivo; }
    public void setEstaActivo(Boolean estaActivo) { this.estaActivo = estaActivo; }
}