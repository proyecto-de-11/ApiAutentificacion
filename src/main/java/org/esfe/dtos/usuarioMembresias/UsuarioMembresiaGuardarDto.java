package org.esfe.dtos.usuarioMembresias;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

public class UsuarioMembresiaGuardarDto {

    @NotNull(message = "El ID del usuario es requerido")
    private Integer usuarioId;

    @NotNull(message = "El ID de la membresía es requerido")
    private Integer membresiaId;

    // fechaInicio ahora es opcional, se generará automáticamente si no se proporciona
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Boolean renovacionAutomatica;

    // Getters y Setters
    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getMembresiaId() {
        return membresiaId;
    }

    public void setMembresiaId(Integer membresiaId) {
        this.membresiaId = membresiaId;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getRenovacionAutomatica() {
        return renovacionAutomatica;
    }

    public void setRenovacionAutomatica(Boolean renovacionAutomatica) {
        this.renovacionAutomatica = renovacionAutomatica;
    }
}
