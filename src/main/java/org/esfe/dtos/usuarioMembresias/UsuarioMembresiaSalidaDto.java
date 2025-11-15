package org.esfe.dtos.usuarioMembresias;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class UsuarioMembresiaSalidaDto {

    private Integer id;
    private UsuarioSimpleDto usuario; // CAMBIO
    private MembresiaSimpleDto membresia; // CAMBIO
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean estaActiva;
    private Boolean renovacionAutomatica;
    private LocalDateTime fechaCreacion;

    // Clases internas para datos relacionados
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

    public static class MembresiaSimpleDto {
        private Integer id;
        private String nombre;
        private String descripcion;
        private BigDecimal precioMensual;
        private Integer maxReservasMes;
        private Integer descuentoPorcentaje;
        private Boolean estaActivo;

        public MembresiaSimpleDto() {}

        public MembresiaSimpleDto(Integer id, String nombre, String descripcion,
                                  BigDecimal precioMensual, Integer maxReservasMes,
                                  Integer descuentoPorcentaje, Boolean estaActivo) {
            this.id = id;
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.precioMensual = precioMensual;
            this.maxReservasMes = maxReservasMes;
            this.descuentoPorcentaje = descuentoPorcentaje;
            this.estaActivo = estaActivo;
        }

        // Getters y setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public BigDecimal getPrecioMensual() { return precioMensual; }
        public void setPrecioMensual(BigDecimal precioMensual) { this.precioMensual = precioMensual; }
        public Integer getMaxReservasMes() { return maxReservasMes; }
        public void setMaxReservasMes(Integer maxReservasMes) { this.maxReservasMes = maxReservasMes; }
        public Integer getDescuentoPorcentaje() { return descuentoPorcentaje; }
        public void setDescuentoPorcentaje(Integer descuentoPorcentaje) { this.descuentoPorcentaje = descuentoPorcentaje; }
        public Boolean getEstaActivo() { return estaActivo; }
        public void setEstaActivo(Boolean estaActivo) { this.estaActivo = estaActivo; }
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public UsuarioSimpleDto getUsuario() { return usuario; }
    public void setUsuario(UsuarioSimpleDto usuario) { this.usuario = usuario; }

    public MembresiaSimpleDto getMembresia() { return membresia; }
    public void setMembresia(MembresiaSimpleDto membresia) { this.membresia = membresia; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Boolean getEstaActiva() { return estaActiva; }
    public void setEstaActiva(Boolean estaActiva) { this.estaActiva = estaActiva; }

    public Boolean getRenovacionAutomatica() { return renovacionAutomatica; }
    public void setRenovacionAutomatica(Boolean renovacionAutomatica) { this.renovacionAutomatica = renovacionAutomatica; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}