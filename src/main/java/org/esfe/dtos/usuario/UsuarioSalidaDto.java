package org.esfe.dtos.usuario;

import java.io.Serializable;

public class UsuarioSalidaDto implements Serializable {

    private Integer id;
    private String email;
    private RolSimpleDto rol; // CAMBIO: de Long idRol a objeto completo
    private Boolean estaActivo;
    private String fechaCreacion;
    private String fechaActualizacion;

    // Clase interna para información simplificada del rol
    public static class RolSimpleDto {
        private Long id;
        private String nombre;
        private String descripcion;

        public RolSimpleDto() {}

        public RolSimpleDto(Long id, String nombre, String descripcion) {
            this.id = id;
            this.nombre = nombre;
            this.descripcion = descripcion;
        }

        // Getters y setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public RolSimpleDto getRol() { return rol; }
    public void setRol(RolSimpleDto rol) { this.rol = rol; }

    public Boolean getEstaActivo() { return estaActivo; }
    public void setEstaActivo(Boolean estaActivo) { this.estaActivo = estaActivo; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(String fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}