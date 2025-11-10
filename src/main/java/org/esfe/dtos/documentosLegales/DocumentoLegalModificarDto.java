package org.esfe.dtos.documentosLegales;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DocumentoLegalModificarDto implements Serializable {

    @NotNull(message = "El id es obligatorio para modificar.")
    @Min(value = 1, message = "El id debe ser un valor positivo.")
    private Integer id;

    @NotNull(message = "El tipo no puede ser nulo.")
    @NotBlank(message = "El tipo es obligatorio.")
    @Size(min = 1, max = 100, message = "El tipo debe tener entre 1 y 100 caracteres.")
    private String tipo;

    @NotNull(message = "El título no puede ser nulo.")
    @NotBlank(message = "El título es obligatorio.")
    @Size(min = 1, max = 255, message = "El título debe tener entre 1 y 255 caracteres.")
    private String titulo;

    @NotNull(message = "El contenido no puede ser nulo.")
    private String contenido;

    @NotNull(message = "La versión no puede ser nula.")
    @NotBlank(message = "La versión es obligatoria.")
    @Size(max = 50, message = "La versión no puede exceder los 50 caracteres.")
    private String version;

    @NotNull(message = "La fecha vigente es obligatoria.")
    private LocalDate fechaVigente;

    @NotNull(message = "El estado (estaActivo) es obligatorio.")
    private Boolean estaActivo;

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public LocalDate getFechaVigente() {
        return fechaVigente;
    }

    public void setFechaVigente(LocalDate fechaVigente) {
        this.fechaVigente = fechaVigente;
    }

    public Boolean getEstaActivo() {
        return estaActivo;
    }

    public void setEstaActivo(Boolean estaActivo) {
        this.estaActivo = estaActivo;
    }
}

