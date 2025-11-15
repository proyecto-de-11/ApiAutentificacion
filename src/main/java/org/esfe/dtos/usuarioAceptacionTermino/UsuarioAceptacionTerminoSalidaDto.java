package org.esfe.dtos.usuarioAceptacionTermino;

import java.io.Serializable;
import java.time.LocalDate;

public class UsuarioAceptacionTerminoSalidaDto implements Serializable {

    private Integer id;
    private UsuarioSimpleDto usuario; // CAMBIO
    private DocumentoLegalSimpleDto documentoLegal; // CAMBIO
    private String fechaAceptacion;

    // Clases internas
    public static class UsuarioSimpleDto {
        private Integer id;
        private String email;

        public UsuarioSimpleDto() {}

        public UsuarioSimpleDto(Integer id, String email) {
            this.id = id;
            this.email = email;
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class DocumentoLegalSimpleDto {
        private Integer id;
        private String tipo;
        private String titulo;
        private String version;
        private LocalDate fechaVigente;
        private Boolean estaActivo;

        public DocumentoLegalSimpleDto() {}

        public DocumentoLegalSimpleDto(Integer id, String tipo, String titulo,
                                       String version, LocalDate fechaVigente, Boolean estaActivo) {
            this.id = id;
            this.tipo = tipo;
            this.titulo = titulo;
            this.version = version;
            this.fechaVigente = fechaVigente;
            this.estaActivo = estaActivo;
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public LocalDate getFechaVigente() { return fechaVigente; }
        public void setFechaVigente(LocalDate fechaVigente) { this.fechaVigente = fechaVigente; }
        public Boolean getEstaActivo() { return estaActivo; }
        public void setEstaActivo(Boolean estaActivo) { this.estaActivo = estaActivo; }
    }

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public UsuarioSimpleDto getUsuario() { return usuario; }
    public void setUsuario(UsuarioSimpleDto usuario) { this.usuario = usuario; }

    public DocumentoLegalSimpleDto getDocumentoLegal() { return documentoLegal; }
    public void setDocumentoLegal(DocumentoLegalSimpleDto documentoLegal) { this.documentoLegal = documentoLegal; }

    public String getFechaAceptacion() { return fechaAceptacion; }
    public void setFechaAceptacion(String fechaAceptacion) { this.fechaAceptacion = fechaAceptacion; }
}

