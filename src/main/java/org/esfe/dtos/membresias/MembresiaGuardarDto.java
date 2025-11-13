package org.esfe.dtos.membresias;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class MembresiaGuardarDto {

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "El título es requerido")
    private String titulo;

    @NotBlank(message = "El contenido es requerido")
    private String contenido;

    @NotBlank(message = "La versión es requerida")
    private String version;

    @NotNull(message = "La fecha de vigencia es requerida")
    private LocalDate fechaVigencia;

    @NotNull(message = "El estado activo es requerido")
    private Boolean estaActivo;

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDate getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(LocalDate fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public Boolean getEstaActivo() {
        return estaActivo;
    }

    public void setEstaActivo(Boolean estaActivo) {
        this.estaActivo = estaActivo;
    }
}

