package org.esfe.dtos.usuarioAceptacionTermino;

import java.io.Serializable;
import java.util.List;
import jakarta.validation.constraints.NotNull;

public class UsuarioAceptacionTerminoGuardarDto implements Serializable {

    @NotNull(message = "El id del usuario es obligatorio.")
    private Integer idUsuario;

    @NotNull(message = "Debe seleccionar al menos un documento legal.")
    private List<Integer> idsDocumentosLegales; // CAMBIO: Lista de IDs

    private String ipAddress; // Opcional: para registrar la IP del usuario

    // Getters y setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<Integer> getIdsDocumentosLegales() {
        return idsDocumentosLegales;
    }

    public void setIdsDocumentosLegales(List<Integer> idsDocumentosLegales) {
        this.idsDocumentosLegales = idsDocumentosLegales;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}