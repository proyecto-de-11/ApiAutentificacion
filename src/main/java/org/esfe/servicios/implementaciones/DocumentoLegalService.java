package org.esfe.servicios.implementaciones;

import org.esfe.dtos.documentosLegales.DocumentoLegalGuardarDto;
import org.esfe.dtos.documentosLegales.DocumentoLegalModificarDto;
import org.esfe.dtos.documentosLegales.DocumentoLegalSalidaDto;
import org.esfe.modelos.DocumentoLegal;
import org.esfe.repositorios.IDocumentoLegalRepository;
import org.esfe.servicios.interfaces.IDocumentoLegalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentoLegalService implements IDocumentoLegalService {

    @Autowired
    private IDocumentoLegalRepository documentoLegalRepository;

    @Autowired
    private ModelMapper modelMapper;

    private DocumentoLegalSalidaDto mapToDto(DocumentoLegal documentoLegal) {
        return modelMapper.map(documentoLegal, DocumentoLegalSalidaDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoLegalSalidaDto> obtenerTodos() {
        // En un servicio real, se debería considerar usar paginación incluso para "obtener todos"
        return documentoLegalRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentoLegalSalidaDto> obtenerPorId(Integer id) {
        return documentoLegalRepository.findById(id).map(this::mapToDto);
    }

    @Override
    public DocumentoLegalSalidaDto crear(DocumentoLegalGuardarDto dto) {
        // Validación de unicidad: tipo y versión no pueden duplicarse
        if (documentoLegalRepository.existsByTipoAndVersion(dto.getTipo(), dto.getVersion())) {
            throw new IllegalArgumentException("Ya existe un Documento Legal con el tipo '" + dto.getTipo() + 
                                               "' y la versión '" + dto.getVersion() + "'.");
        }

        DocumentoLegal documento = modelMapper.map(dto, DocumentoLegal.class);
        // Asegurar que el ID sea null para crear un nuevo registro
        documento.setId(null); 
        documento.setFechaCreacion(LocalDateTime.now());
        
        DocumentoLegal guardado = documentoLegalRepository.save(documento);

        return mapToDto(guardado);
    }

    @Override
    public DocumentoLegalSalidaDto editar(DocumentoLegalModificarDto dto) {
        // 1. Verificar existencia
        DocumentoLegal existente = documentoLegalRepository.findById(dto.getId())
                .orElseThrow(() -> new NoSuchElementException("Documento Legal no encontrado con id: " + dto.getId()));

        // 2. Verificar unicidad de tipo y versión (excluyendo el propio documento)
        boolean otroConMismoTipoYVersion = documentoLegalRepository.existsByTipoAndVersion(dto.getTipo(), dto.getVersion());
        
        if (otroConMismoTipoYVersion && (existente.getTipo() == null || !existente.getTipo().equals(dto.getTipo()) || 
                                         existente.getVersion() == null || !existente.getVersion().equals(dto.getVersion()))
        ) {
             throw new IllegalArgumentException("Otro Documento Legal ya existe con el tipo '" + dto.getTipo() + 
                                               "' y la versión '" + dto.getVersion() + "'.");
        }

        // 3. Mapear los campos modificados al objeto existente
        modelMapper.map(dto, existente);

        DocumentoLegal actualizado = documentoLegalRepository.save(existente);
        
        return mapToDto(actualizado);
    }

    @Override
    public void eliminarPorId(Integer id) {
        if (!documentoLegalRepository.existsById(id)) {
            throw new NoSuchElementException("Documento Legal no encontrado con id: " + id);
        }
        documentoLegalRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentoLegalSalidaDto> obtenerDocumentosPaginadosYFiltrados(Optional<String> busqueda, Pageable pageable) {
        
        // **Asegurar orden DESC por ID si no se especifica otra cosa en Pageable**
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                pageable.getPageNumber(), 
                pageable.getPageSize(), 
                // Requerimiento: ordenar por ID de forma descendente
                Sort.by("id").descending()
            );
        }

        Page<DocumentoLegal> page;
        String textoBusqueda = busqueda.orElse("").trim();

        if (!textoBusqueda.isBlank()) {
            // Usa el método del repositorio para buscar por tipo O título
            page = documentoLegalRepository.findByTipoContainingIgnoreCaseOrTituloContainingIgnoreCase(textoBusqueda, textoBusqueda, pageable);
        } else {
            // Sin búsqueda, solo paginación
            page = documentoLegalRepository.findAll(pageable);
        }

        // Mapear la página de modelos a una lista de DTOs
        List<DocumentoLegalSalidaDto> dtoList = page.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        // Crear y retornar la PageImpl con los DTOs y la metadata de paginación
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorTipoYVersion(String tipo, String version) {
        return documentoLegalRepository.existsByTipoAndVersion(tipo, version);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoLegalSalidaDto> buscarPorTipo(String tipo) {
        return documentoLegalRepository.findByTipo(tipo).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoLegalSalidaDto> buscarPorFechaVigenciaAnteriorA(LocalDate fecha) {
        return documentoLegalRepository.findByFechaVigenteBefore(fecha).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
}