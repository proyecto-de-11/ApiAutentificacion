package org.esfe.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_aceptacion_terminos")
public class UsuarioAceptacionTermino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idDocumentoLegal", nullable = false)
    private DocumentoLegal documentoLegal;

    @Column(name = "fecha_aceptacion", nullable = false, columnDefinition = "DATETIME")
    private String fechaAceptacion;

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

    public DocumentoLegal getDocumentoLegal() {
        return documentoLegal;
    }

    public void setDocumentoLegal(DocumentoLegal documentoLegal) {
        this.documentoLegal = documentoLegal;
    }

    public String getFechaAceptacion() {
        return fechaAceptacion;
    }

    public void setFechaAceptacion(String fechaAceptacion) {
        this.fechaAceptacion = fechaAceptacion;
    }
}
