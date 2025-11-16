package org.esfe.modelos;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario_aceptacion_terminos")
public class UsuarioAceptacionTermino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario", nullable = false, unique = true)
    private Usuario usuario;

    // CAMBIO: Relación Many-to-Many con DocumentoLegal
    @OneToMany(mappedBy = "usuarioAceptacionTermino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioDocumentoLegal> documentosAceptados = new ArrayList<>();

    @Column(name = "fecha_creacion", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", columnDefinition = "DATETIME")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Métodos helper para manejar la relación bidireccional
    public void addDocumentoLegal(DocumentoLegal documentoLegal, String ipAddress) {
        UsuarioDocumentoLegal relacion = new UsuarioDocumentoLegal();
        relacion.setUsuarioAceptacionTermino(this);
        relacion.setDocumentoLegal(documentoLegal);
        relacion.setIpAddress(ipAddress);
        relacion.setFechaAceptacion(LocalDateTime.now());
        this.documentosAceptados.add(relacion);
    }

    public void removeDocumentoLegal(DocumentoLegal documentoLegal) {
        this.documentosAceptados.removeIf(rel ->
                rel.getDocumentoLegal().getId().equals(documentoLegal.getId())
        );
    }

    public void clearDocumentosLegales() {
        this.documentosAceptados.clear();
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<UsuarioDocumentoLegal> getDocumentosAceptados() {
        return documentosAceptados;
    }

    public void setDocumentosAceptados(List<UsuarioDocumentoLegal> documentosAceptados) {
        this.documentosAceptados = documentosAceptados;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}