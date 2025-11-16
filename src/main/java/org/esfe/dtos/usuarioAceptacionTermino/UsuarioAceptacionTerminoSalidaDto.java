package org.esfe.dtos.usuarioAceptacionTermino;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UsuarioAceptacionTerminoSalidaDto implements Serializable {

    private Integer id;
    private UsuarioSimpleDto usuario;
    private List<DocumentoLegalAceptadoDto> documentosAceptados; // CAMBIO: Lista de documentos
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // Clases internas
    public static class UsuarioSimpleDto {
        private Integer id;
        private String email;
        private Boolean estaActivo;

        public UsuarioSimpleDto() {}

        public UsuarioSimpleDto(Integer id, String email, Boolean estaActivo) {
            this.id = id;
            this.email = email;
            this.estaActivo = estaActivo;
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Boolean getEstaActivo() { return estaActivo; }
        public void setEstaActivo(Boolean estaActivo) { this.estaActivo = estaActivo; }
    }

    public static class DocumentoLegalAceptadoDto {
        private Integer id;
        private Integer documentoLegalId;
        private String tipo;
        private String titulo;
        private String version;
        private LocalDate fechaVigente;
        private Boolean estaActivo;
        private LocalDateTime fechaAceptacion;
        private String ipAddress;

        public DocumentoLegalAceptadoDto() {}

        public DocumentoLegalAceptadoDto(Integer id, Integer documentoLegalId, String tipo, String titulo,
                                         String version, LocalDate fechaVigente, Boolean estaActivo,
                                         LocalDateTime fechaAceptacion, String ipAddress) {
            this.id = id;
            this.documentoLegalId = documentoLegalId;
            this.tipo = tipo;
            this.titulo = titulo;
            this.version = version;
            this.fechaVigente = fechaVigente;
            this.estaActivo = estaActivo;
            this.fechaAceptacion = fechaAceptacion;
            this.ipAddress = ipAddress;
        }

        // Getters y setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public Integer getDocumentoLegalId() { return documentoLegalId; }
        public void setDocumentoLegalId(Integer documentoLegalId) { this.documentoLegalId = documentoLegalId; }
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
        public LocalDateTime getFechaAceptacion() { return fechaAceptacion; }
        public void setFechaAceptacion(LocalDateTime fechaAceptacion) { this.fechaAceptacion = fechaAceptacion; }
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    }

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public UsuarioSimpleDto getUsuario() { return usuario; }
    public void setUsuario(UsuarioSimpleDto usuario) { this.usuario = usuario; }

    public List<DocumentoLegalAceptadoDto> getDocumentosAceptados() { return documentosAceptados; }
    public void setDocumentosAceptados(List<DocumentoLegalAceptadoDto> documentosAceptados) {
        this.documentosAceptados = documentosAceptados;
    }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
