package org.esfe.servicios.implementaciones;

import org.esfe.dtos.usuarioMembresias.UsuarioMembresiaGuardarDto;
import org.esfe.dtos.usuarioMembresias.UsuarioMembresiaModificarDto;
import org.esfe.dtos.usuarioMembresias.UsuarioMembresiaSalidaDto;
import org.esfe.modelos.Membresia;
import org.esfe.modelos.Usuario;
import org.esfe.modelos.UsuarioMembresia;
import org.esfe.repositorios.IUsuarioMembresiaRepository;
import org.esfe.servicios.interfaces.IUsuarioMembresiaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioMembresiaService implements IUsuarioMembresiaService {

    @Autowired
    private IUsuarioMembresiaRepository usuarioMembresiaRepository;

    @Autowired
    private ModelMapper modelMapper;

    private UsuarioMembresiaSalidaDto mapToDto(UsuarioMembresia um) {
        UsuarioMembresiaSalidaDto dto = modelMapper.map(um, UsuarioMembresiaSalidaDto.class);
        if (um.getUsuario() != null) dto.setUsuarioId(um.getUsuario().getId());
        if (um.getMembresia() != null) dto.setMembresiaId(um.getMembresia().getId());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioMembresiaSalidaDto> obtenerTodos() {
        return usuarioMembresiaRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioMembresiaSalidaDto> obtenerPorId(Integer id) {
        return usuarioMembresiaRepository.findById(id).map(this::mapToDto);
    }

    @Override
    public UsuarioMembresiaSalidaDto crear(UsuarioMembresiaGuardarDto dto) {
        // Verificar si el usuario ya tiene una membresía asignada
        if (usuarioMembresiaRepository.findByUsuarioId(dto.getUsuarioId()).isPresent()) {
            throw new IllegalArgumentException("El usuario ya tiene una membresía asignada.");
        }

        UsuarioMembresia um = new UsuarioMembresia();
        Usuario u = new Usuario();
        u.setId(dto.getUsuarioId());
        um.setUsuario(u);

        Membresia m = new Membresia();
        m.setId(dto.getMembresiaId());
        um.setMembresia(m);

        um.setFechaInicio(dto.getFechaInicio());
        um.setFechaFin(dto.getFechaFin());
        um.setRenovacionAutomatica(dto.getRenovacionAutomatica() != null ? dto.getRenovacionAutomatica() : false);
        um.setEstaActiva(true);
        um.setFechaCreacion(LocalDateTime.now());

        UsuarioMembresia guardada = usuarioMembresiaRepository.save(um);
        return mapToDto(guardada);
    }

    @Override
    public UsuarioMembresiaSalidaDto editar(UsuarioMembresiaModificarDto dto) {
        UsuarioMembresia existente = usuarioMembresiaRepository.findById(dto.getId())
                .orElseThrow(() -> new NoSuchElementException("UsuarioMembresia no encontrada con id: " + dto.getId()));

        if (dto.getFechaInicio() != null) existente.setFechaInicio(dto.getFechaInicio());
        if (dto.getFechaFin() != null) existente.setFechaFin(dto.getFechaFin());
        if (dto.getEstaActiva() != null) existente.setEstaActiva(dto.getEstaActiva());
        if (dto.getRenovacionAutomatica() != null) existente.setRenovacionAutomatica(dto.getRenovacionAutomatica());

        UsuarioMembresia actualizado = usuarioMembresiaRepository.save(existente);
        return mapToDto(actualizado);
    }

    @Override
    public void eliminarPorId(Integer id) {
        if (!usuarioMembresiaRepository.existsById(id)) {
            throw new NoSuchElementException("UsuarioMembresia no encontrada con id: " + id);
        }
        usuarioMembresiaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioMembresiaSalidaDto> obtenerPaginados(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
        }
        Page<UsuarioMembresia> page = usuarioMembresiaRepository.findAll(pageable);
        List<UsuarioMembresiaSalidaDto> dtoList = page.stream().map(this::mapToDto).collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioMembresiaSalidaDto> obtenerPorUsuario(Integer usuarioId) {
        return usuarioMembresiaRepository.findByUsuarioId(usuarioId).map(this::mapToDto);
    }
}
