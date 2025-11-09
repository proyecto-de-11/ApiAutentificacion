package org.esfe.dtos.tiposdeporte;

import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TiposDeporteGuardarDto implements Serializable {

    @NotNull(message = "El nombre no puede ser nulo.")
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres.")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres.")
    private String descripcion;

    @Size(max = 1000, message = "El icono no puede exceder los 1000 caracteres.")
    private String icono;

    @NotNull(message = "El estado (estaActivo) es obligatorio.")
    private Boolean estaActivo;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public Boolean getEstaActivo() {
        return estaActivo;
    }

    public void setEstaActivo(Boolean estaActivo) {
        this.estaActivo = estaActivo;
    }
}

