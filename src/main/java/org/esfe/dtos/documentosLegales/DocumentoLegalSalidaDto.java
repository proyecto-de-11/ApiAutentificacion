package org.esfe.dtos.documentosLegales;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DocumentoLegalSalidaDto implements Serializable {
    private Integer id;
    private String tipo;
    private String titulo;
    private String contenido;
    private String version;
    private LocalDate fechaVigente;
    private Boolean estaActivo;
    private LocalDateTime fechaCreacion;

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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}

