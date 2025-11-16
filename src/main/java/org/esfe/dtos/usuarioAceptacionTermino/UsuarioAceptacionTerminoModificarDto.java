package org.esfe.dtos.usuarioAceptacionTermino;

import java.io.Serializable;
import java.util.List;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UsuarioAceptacionTerminoModificarDto implements Serializable {

    @NotNull(message = "El id de la aceptación es obligatorio.")
    @Min(value = 1, message = "El id debe ser positivo.")
    private Integer id;

    private List<Integer> idsDocumentosLegales; // Lista de IDs de documentos a agregar/reemplazar

    private String ipAddress;

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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