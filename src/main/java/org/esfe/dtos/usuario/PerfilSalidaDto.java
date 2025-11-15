package org.esfe.dtos.usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PerfilSalidaDto {
    private Integer id;
    private UsuarioSimpleDto usuario; // CAMBIO: de Integer usuarioId a objeto
    private String nombreCompleto;
    private String telefono;
    private String documentoIdentidad;
    private LocalDate fechaNacimiento;
    private String genero;
    private String fotoPerfil;
    private String biografia;
    private String ciudad;
    private String pais;
    private LocalDateTime fechaGuardado;
    private LocalDateTime fechaActualizacion;

    public static class UsuarioSimpleDto {
        private Integer id;
        private String email;
        private Boolean estaActivo;

        public UsuarioSimpleDto() {}

        public UsuarioSimpleDto(Integer id, String email, Boolean estaActivo) {
            this.id = id;
            this.email = email;
            this.estaActivo = estaActivo;
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Boolean getEstaActivo() { return estaActivo; }
        public void setEstaActivo(Boolean estaActivo) { this.estaActivo = estaActivo; }
    }

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public UsuarioSimpleDto getUsuario() { return usuario; }
    public void setUsuario(UsuarioSimpleDto usuario) { this.usuario = usuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public LocalDateTime getFechaGuardado() { return fechaGuardado; }
    public void setFechaGuardado(LocalDateTime fechaGuardado) { this.fechaGuardado = fechaGuardado; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}