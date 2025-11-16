package org.esfe.servicios.implementaciones;

import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoGuardarDto;
import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoModificarDto;
import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoSalidaDto;
import org.esfe.modelos.DocumentoLegal;
import org.esfe.modelos.Usuario;
import org.esfe.modelos.UsuarioAceptacionTermino;
import org.esfe.modelos.UsuarioDocumentoLegal;
import org.esfe.repositorios.IDocumentoLegalRepository;
import org.esfe.repositorios.IUsuarioAceptacionTerminoRepository;
import org.esfe.repositorios.IUsuarioRepository;
import org.esfe.servicios.interfaces.IUsuarioAceptacionTerminoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioAceptacionTerminoService implements IUsuarioAceptacionTerminoService {

    private final IUsuarioAceptacionTerminoRepository repo;
    private final IUsuarioRepository usuarioRepository;
    private final IDocumentoLegalRepository documentoRepository;

    public UsuarioAceptacionTerminoService(
            IUsuarioAceptacionTerminoRepository repo,
            IUsuarioRepository usuarioRepository,
            IDocumentoLegalRepository documentoRepository) {
        this.repo = repo;
        this.usuarioRepository = usuarioRepository;
        this.documentoRepository = documentoRepository;
    }

    @Override
    public UsuarioAceptacionTerminoSalidaDto crear(UsuarioAceptacionTerminoGuardarDto dto) {
        // Validar que el usuario exista
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + dto.getIdUsuario()));

        // Validar que el usuario no tenga ya aceptaciones registradas (OneToOne)
        Optional<UsuarioAceptacionTermino> existente = repo.findByUsuario_Id(dto.getIdUsuario());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("El usuario ya tiene aceptaciones registradas. Use el método de actualización.");
        }

        // Validar que se hayan proporcionado documentos
        if (dto.getIdsDocumentosLegales() == null || dto.getIdsDocumentosLegales().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un documento legal");
        }

        UsuarioAceptacionTermino entity = new UsuarioAceptacionTermino();
        entity.setUsuario(usuario);

        // Agregar los documentos legales
        for (Integer docId : dto.getIdsDocumentosLegales()) {
            DocumentoLegal documento = documentoRepository.findById(docId)
                    .orElseThrow(() -> new NoSuchElementException("Documento legal no encontrado con id: " + docId));
            entity.addDocumentoLegal(documento, dto.getIpAddress());
        }

        UsuarioAceptacionTermino guardado = repo.save(entity);
        return mapToDto(guardado);
    }

    @Override
    public UsuarioAceptacionTerminoSalidaDto editar(UsuarioAceptacionTerminoModificarDto dto) {
        UsuarioAceptacionTermino existente = repo.findById(dto.getId())
                .orElseThrow(() -> new NoSuchElementException("Aceptación no encontrada con id: " + dto.getId()));

        // Si se proporcionan nuevos documentos legales, reemplazar los existentes
        if (dto.getIdsDocumentosLegales() != null) {
            if (dto.getIdsDocumentosLegales().isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar al menos un documento legal");
            }

            // Limpiar los documentos existentes
            existente.clearDocumentosLegales();

            // Agregar los nuevos documentos
            for (Integer docId : dto.getIdsDocumentosLegales()) {
                DocumentoLegal documento = documentoRepository.findById(docId)
                        .orElseThrow(() -> new NoSuchElementException("Documento legal no encontrado con id: " + docId));
                existente.addDocumentoLegal(documento, dto.getIpAddress());
            }
        }

        UsuarioAceptacionTermino actualizado = repo.save(existente);
        return mapToDto(actualizado);
    }

    @Override
    public List<UsuarioAceptacionTerminoSalidaDto> obtenerTodos() {
        return repo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<UsuarioAceptacionTerminoSalidaDto> obtenerPorId(Integer id) {
        return repo.findById(id).map(this::mapToDto);
    }

    @Override
    public void eliminarPorId(Integer id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Aceptación no encontrada con id: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioAceptacionTerminoSalidaDto> obtenerPaginadoYFiltrado(
            Optional<Integer> idUsuario,
            Optional<Integer> idDocumentoLegal,
            Pageable pageable) {

        Page<UsuarioAceptacionTermino> page;
        boolean usuarioPresente = idUsuario != null && idUsuario.isPresent();
        boolean documentoPresente = idDocumentoLegal != null && idDocumentoLegal.isPresent();

        if (!usuarioPresente && !documentoPresente) {
            page = repo.findAll(pageable);
        } else if (usuarioPresente) {
            page = repo.findByUsuario_Id(idUsuario.get(), pageable);
        } else {
            page = repo.findByDocumentosAceptados_DocumentoLegal_Id(idDocumentoLegal.get(), pageable);
        }

        return page.map(this::mapToDto);
    }

    // Mapeo de Entity a DTO
    private UsuarioAceptacionTerminoSalidaDto mapToDto(UsuarioAceptacionTermino entity) {
        if (entity == null) return null;

        UsuarioAceptacionTerminoSalidaDto dto = new UsuarioAceptacionTerminoSalidaDto();
        dto.setId(entity.getId());

        // Mapear usuario completo
        if (entity.getUsuario() != null) {
            Usuario u = entity.getUsuario();
            UsuarioAceptacionTerminoSalidaDto.UsuarioSimpleDto usuarioDto =
                    new UsuarioAceptacionTerminoSalidaDto.UsuarioSimpleDto(
                            u.getId(),
                            u.getEmail(),
                            u.getEstaActivo()
                    );
            dto.setUsuario(usuarioDto);
        }

        // Mapear documentos legales aceptados
        List<UsuarioAceptacionTerminoSalidaDto.DocumentoLegalAceptadoDto> docsDto =
                entity.getDocumentosAceptados().stream()
                        .map(rel -> {
                            DocumentoLegal doc = rel.getDocumentoLegal();
                            return new UsuarioAceptacionTerminoSalidaDto.DocumentoLegalAceptadoDto(
                                    rel.getId(),
                                    doc.getId(),
                                    doc.getTipo(),
                                    doc.getTitulo(),
                                    doc.getVersion(),
                                    doc.getFechaVigente(),
                                    doc.getEstaActivo(),
                                    rel.getFechaAceptacion(),
                                    rel.getIpAddress()
                            );
                        })
                        .collect(Collectors.toList());
        dto.setDocumentosAceptados(docsDto);

        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());

        return dto;
    }
}