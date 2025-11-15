package org.esfe.dtos.usuarioAceptacionTermino;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

public class UsuarioAceptacionTerminoGuardarDto implements Serializable {

    @NotNull(message = "El id del usuario es obligatorio.")
    private Integer idUsuario;

    @NotNull(message = "El id del documento legal es obligatorio.")
    private Integer idDocumentoLegal;

    // La fecha de aceptación la asigna el servidor al crear; no debe ser obligatoria en el DTO de guardado.
    private String fechaAceptacion;

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
