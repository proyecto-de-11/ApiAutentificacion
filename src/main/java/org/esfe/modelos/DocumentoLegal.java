package org.esfe.modelos;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos_legales")
public class DocumentoLegal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "contenido", columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "fecha_vigente", nullable = false, columnDefinition = "DATE")
    private LocalDate fechaVigente;

    @Column(name = "esta_activo", nullable = false, columnDefinition = "BOOLEAN")
    private Boolean estaActivo;

    @Column(name = "fecha_creacion", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime fechaCreacion;
}
