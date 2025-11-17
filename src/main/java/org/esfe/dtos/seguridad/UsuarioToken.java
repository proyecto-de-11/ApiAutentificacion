package org.esfe.dtos.seguridad;

public class UsuarioToken {
    private String token;

    public UsuarioToken() {}

    public UsuarioToken(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}