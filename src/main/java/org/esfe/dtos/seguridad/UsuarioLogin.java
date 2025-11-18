package org.esfe.dtos.seguridad;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioLogin {
    @Email(message = "El correo debe ser válido")
    @NotBlank(message = "El correo es requerido")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    private String password;

    public UsuarioLogin() {}

    public UsuarioLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}