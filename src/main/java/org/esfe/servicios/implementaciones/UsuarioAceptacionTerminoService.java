package org.esfe.servicios.implementaciones;

import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoGuardarDto;
import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoModificarDto;
import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoSalidaDto;
import org.esfe.modelos.DocumentoLegal;
import org.esfe.modelos.Usuario;
import org.esfe.modelos.UsuarioAceptacionTermino;
import org.esfe.repositorios.IDocumentoLegalRepository;
import org.esfe.repositorios.IUsuarioAceptacionTerminoRepository;
import org.esfe.repositorios.IUsuarioRepository;
import org.esfe.servicios.interfaces.IUsuarioAceptacionTerminoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public UsuarioAceptacionTerminoService(IUsuarioAceptacionTerminoRepository repo, IUsuarioRepository usuarioRepository, IDocumentoLegalRepository documentoRepository) {
        this.repo = repo;
        this.usuarioRepository = usuarioRepository;
        this.documentoRepository = documentoRepository;
    }

    private UsuarioAceptacionTerminoSalidaDto mapToDto(UsuarioAceptacionTermino entity) {
        if (entity == null) return null;
        UsuarioAceptacionTerminoSalidaDto dto = new UsuarioAceptacionTerminoSalidaDto();
        dto.setId(entity.getId());
        dto.setIdUsuario(entity.getUsuario() != null ? entity.getUsuario().getId() : null);
        dto.setIdDocumentoLegal(entity.getDocumentoLegal() != null ? entity.getDocumentoLegal().getId() : null);
        dto.setFechaAceptacion(entity.getFechaAceptacion());
        return dto;
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
    public UsuarioAceptacionTerminoSalidaDto crear(UsuarioAceptacionTerminoGuardarDto dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + dto.getIdUsuario()));

        DocumentoLegal documento = documentoRepository.findById(dto.getIdDocumentoLegal())
                .orElseThrow(() -> new NoSuchElementException("Documento legal no encontrado con id: " + dto.getIdDocumentoLegal()));

        UsuarioAceptacionTermino entity = new UsuarioAceptacionTermino();
        entity.setUsuario(usuario);
        entity.setDocumentoLegal(documento);
        // Asignar la fecha y hora actual al crear el registro (formato compatible con DATETIME)
        String ahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        entity.setFechaAceptacion(ahora);

        UsuarioAceptacionTermino guardado = repo.save(entity);
        return mapToDto(guardado);
    }

    @Override
    public UsuarioAceptacionTerminoSalidaDto editar(UsuarioAceptacionTerminoModificarDto dto) {
        UsuarioAceptacionTermino existente = repo.findById(dto.getId())
                .orElseThrow(() -> new NoSuchElementException("Aceptación no encontrada con id: " + dto.getId()));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + dto.getIdUsuario()));

        DocumentoLegal documento = documentoRepository.findById(dto.getIdDocumentoLegal())
                .orElseThrow(() -> new NoSuchElementException("Documento legal no encontrado con id: " + dto.getIdDocumentoLegal()));

        existente.setUsuario(usuario);
        existente.setDocumentoLegal(documento);
        // Actualizar fechaAceptacion solo si se proporciona en el DTO
        if (dto.getFechaAceptacion() != null && !dto.getFechaAceptacion().isBlank()) {
            existente.setFechaAceptacion(dto.getFechaAceptacion());
        }

        UsuarioAceptacionTermino actualizado = repo.save(existente);
        return mapToDto(actualizado);
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
    public Page<UsuarioAceptacionTerminoSalidaDto> obtenerPaginadoYFiltrado(Optional<Integer> idUsuario, Optional<Integer> idDocumentoLegal, Pageable pageable) {
        Page<UsuarioAceptacionTermino> page;
        boolean usuarioPresente = idUsuario != null && idUsuario.isPresent();
        boolean documentoPresente = idDocumentoLegal != null && idDocumentoLegal.isPresent();

        if (!usuarioPresente && !documentoPresente) {
            page = repo.findAll(pageable);
        } else if (usuarioPresente) {
            page = repo.findByUsuario_Id(idUsuario.get(), pageable);
        } else {
            page = repo.findByDocumentoLegal_Id(idDocumentoLegal.get(), pageable);
        }

        return page.map(this::mapToDto);
    }
}
