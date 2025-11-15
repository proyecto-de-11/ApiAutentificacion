package org.esfe.dtos.usuarioAceptacionTermino;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UsuarioAceptacionTerminoModificarDto implements Serializable {

    @NotNull(message = "El id de la aceptación es obligatorio.")
    @Min(value = 1, message = "El id debe ser positivo.")
    private Integer id;

    @NotNull(message = "El id del usuario es obligatorio.")
    private Integer idUsuario;

    @NotNull(message = "El id del documento legal es obligatorio.")
    private Integer idDocumentoLegal;

    // La fecha de aceptación es opcional en la modificación; si no viene, se mantiene la existente.
    private String fechaAceptacion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdDocumentoLegal() {
        return idDocumentoLegal;
    }

    public void setIdDocumentoLegal(Integer idDocumentoLegal) {
        this.idDocumentoLegal = idDocumentoLegal;
    }

    public String getFechaAceptacion() {
        return fechaAceptacion;
    }

    public void setFechaAceptacion(String fechaAceptacion) {
        this.fechaAceptacion = fechaAceptacion;
    }
}
