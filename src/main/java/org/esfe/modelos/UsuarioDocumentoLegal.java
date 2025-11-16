package org.esfe.modelos;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_documento_legal")
public class UsuarioDocumentoLegal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_aceptacion_termino_id", nullable = false)
    private UsuarioAceptacionTermino usuarioAceptacionTermino;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "documento_legal_id", nullable = false)
    private DocumentoLegal documentoLegal;

    @Column(name = "fecha_aceptacion", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime fechaAceptacion;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @PrePersist
    public void prePersist() {
        if (this.fechaAceptacion == null) {
            this.fechaAceptacion = LocalDateTime.now();
        }
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UsuarioAceptacionTermino getUsuarioAceptacionTermino() {
        return usuarioAceptacionTermino;
    }

    public void setUsuarioAceptacionTermino(UsuarioAceptacionTermino usuarioAceptacionTermino) {
        this.usuarioAceptacionTermino = usuarioAceptacionTermino;
    }

    public DocumentoLegal getDocumentoLegal() {
        return documentoLegal;
    }

    public void setDocumentoLegal(DocumentoLegal documentoLegal) {
        this.documentoLegal = documentoLegal;
    }

    public LocalDateTime getFechaAceptacion() {
        return fechaAceptacion;
    }

    public void setFechaAceptacion(LocalDateTime fechaAceptacion) {
        this.fechaAceptacion = fechaAceptacion;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}