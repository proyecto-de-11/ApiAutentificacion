package org.esfe.servicios.interfaces;

import org.esfe.dtos.documentosLegales.DocumentoLegalGuardarDto;
import org.esfe.dtos.documentosLegales.DocumentoLegalModificarDto;
import org.esfe.dtos.documentosLegales.DocumentoLegalSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IDocumentoLegalService {

    /**
     * Obtiene todos los documentos legales sin paginar.
     */
    List<DocumentoLegalSalidaDto> obtenerTodos();

    /**
     * Obtiene un documento legal por su ID.
     */
    Optional<DocumentoLegalSalidaDto> obtenerPorId(Integer id);

    /**
     * Crea un nuevo documento legal.
     */
    DocumentoLegalSalidaDto crear(DocumentoLegalGuardarDto dto);

    /**
     * Edita un documento legal existente.
     */
    DocumentoLegalSalidaDto editar(DocumentoLegalModificarDto dto);

    /**
     * Elimina un documento legal por su ID.
     */
    void eliminarPorId(Integer id);

    /**
     * Obtiene documentos legales paginados, con opción de búsqueda por tipo o título.
     * La paginación y ordenamiento (por defecto DESC por ID) se maneja con Pageable.
     */
    Page<DocumentoLegalSalidaDto> obtenerDocumentosPaginadosYFiltrados(Optional<String> busqueda, Pageable pageable);

    /**
     * Verifica si existe un documento con el tipo y versión especificados.
     */
    boolean existePorTipoYVersion(String tipo, String version);

    /**
     * Busca documentos por tipo exacto.
     */
    List<DocumentoLegalSalidaDto> buscarPorTipo(String tipo);

    /**
     * Busca documentos cuya fecha de vigencia es anterior a la fecha dada.
     */
    List<DocumentoLegalSalidaDto> buscarPorFechaVigenciaAnteriorA(LocalDate fecha);
}