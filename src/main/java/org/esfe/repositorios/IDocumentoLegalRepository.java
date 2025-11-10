package org.esfe.repositorios;

import org.esfe.modelos.DocumentoLegal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IDocumentoLegalRepository extends JpaRepository<DocumentoLegal, Integer> {
    // Buscar por tipo exacto
    List<DocumentoLegal> findByTipo(String tipo);

    // Buscar por coincidencia parcial en tipo (insensible a mayúsculas)
    List<DocumentoLegal> findByTipoContainingIgnoreCase(String texto);

    // Buscar por título parcial (insensible a mayúsculas)
    List<DocumentoLegal> findByTituloContainingIgnoreCase(String texto);

    // Buscar por estado activo/inactivo
    List<DocumentoLegal> findByEstaActivo(Boolean estaActivo);

    // Buscar documentos cuya fecha de vigencia sea anterior a una fecha dada
    List<DocumentoLegal> findByFechaVigenteBefore(LocalDate fecha);

    // Paginado y búsqueda por tipo o título (útil para endpoints con paginación)
    Page<DocumentoLegal> findByTipoContainingIgnoreCaseOrTituloContainingIgnoreCase(String tipo, String titulo, Pageable pageable);

    // Verificar existencia por tipo y versión (útil para evitar duplicados)
    boolean existsByTipoAndVersion(String tipo, String version);
}
