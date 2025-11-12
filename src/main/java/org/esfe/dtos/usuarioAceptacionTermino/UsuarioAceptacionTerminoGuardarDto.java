package org.esfe.dtos.usuarioAceptacionTermino;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioAceptacionTerminoGuardarDto implements Serializable {

    @NotNull(message = "El id del usuario es obligatorio.")
    private Integer idUsuario;

    @NotNull(message = "El id del documento legal es obligatorio.")
    private Integer idDocumentoLegal;

    @NotNull(message = "La fecha de aceptación es obligatoria.")
    @NotBlank(message = "La fecha de aceptación no puede estar en blanco.")
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

