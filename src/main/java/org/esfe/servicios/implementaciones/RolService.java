package org.esfe.servicios.implementaciones;

import org.esfe.dtos.RolGuardarDto;
import org.esfe.dtos.RolModificarDto;
import org.esfe.dtos.RolSalidaDto;
import org.esfe.modelos.Rol;
import org.esfe.repositorios.IRolRepository;
import org.esfe.servicios.interfaces.IRolService;
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
public class RolService implements IRolService {

    private final IRolRepository rolRepository;

    // Constructor explícito (inyección por constructor)
    public RolService(IRolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    private RolSalidaDto mapToDto(Rol rol) {
        if (rol == null) return null;
        RolSalidaDto dto = new RolSalidaDto();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());
        return dto;
    }

    @Override
    public List<RolSalidaDto> obtenerTodos() {
        return rolRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RolSalidaDto> obtenerPorId(Long id) {
        return rolRepository.findById(id).map(this::mapToDto);
    }

    @Override
    public RolSalidaDto crear(RolGuardarDto rolGuardarDto) {
        // Verificar existencia por nombre (ignorando mayúsculas)
        rolRepository.findByNombreIgnoreCase(rolGuardarDto.getNombre()).ifPresent(r -> {
            throw new IllegalArgumentException("Ya existe un rol con el nombre proporcionado.");
        });

        Rol rol = new Rol();
        rol.setNombre(rolGuardarDto.getNombre());
        rol.setDescripcion(rolGuardarDto.getDescripcion());

        Rol guardado = rolRepository.save(rol);
        return mapToDto(guardado);
    }

    @Override
    public RolSalidaDto editar(RolModificarDto rolModificarDto) {
        Rol existente = rolRepository.findById(rolModificarDto.getId())
                .orElseThrow(() -> new NoSuchElementException("Rol no encontrado con id: " + rolModificarDto.getId()));

        // Verificar duplicado de nombre en otro registro
        rolRepository.findByNombreIgnoreCase(rolModificarDto.getNombre())
                .filter(r -> !r.getId().equals(rolModificarDto.getId()))
                .ifPresent(r -> { throw new IllegalArgumentException("Otro rol con ese nombre ya existe."); });

        existente.setNombre(rolModificarDto.getNombre());
        existente.setDescripcion(rolModificarDto.getDescripcion());

        Rol actualizado = rolRepository.save(existente);
        return mapToDto(actualizado);
    }

    @Override
    public void eliminarPorId(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new NoSuchElementException("Rol no encontrado con id: " + id);
        }
        rolRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RolSalidaDto> obtenerRolesPaginadosYFiltrados(Optional<String> busqueda, Pageable pageable) {
        Page<Rol> page;
        // Corregir manejo del Optional<String>
        if (busqueda == null || busqueda.isEmpty() || busqueda.get().isBlank()) {
            page = rolRepository.findAll(pageable);
        } else {
            String q = busqueda.get().trim();
            page = rolRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(q, q, pageable);
        }
        return page.map(this::mapToDto);
    }

}
