package org.esfe.servicios.implementaciones;

import org.esfe.dtos.usuario.UsuarioGuardarDto;
import org.esfe.dtos.usuario.UsuarioModificarDto;
import org.esfe.dtos.usuario.UsuarioSalidaDto;
import org.esfe.modelos.Rol;
import org.esfe.modelos.Usuario;
import org.esfe.repositorios.IRolRepository;
import org.esfe.repositorios.IUsuarioRepository;
import org.esfe.servicios.interfaces.IUsuarioService;
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
public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final IRolRepository rolRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository, IRolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    private UsuarioSalidaDto mapToDto(Usuario usuario) {
        if (usuario == null) return null;
        UsuarioSalidaDto dto = new UsuarioSalidaDto();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setIdRol(usuario.getRol() != null ? usuario.getRol().getId() : null);
        dto.setEstaActivo(usuario.getEstaActivo());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        dto.setFechaActualizacion(usuario.getFechaActualizacion());
        return dto;
    }

    @Override
    public List<UsuarioSalidaDto> obtenerTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UsuarioSalidaDto> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id).map(this::mapToDto);
    }

    @Override
    public UsuarioSalidaDto crear(UsuarioGuardarDto usuarioGuardarDto) {
        // Verificar que no exista por email
        usuarioRepository.findByEmailIgnoreCase(usuarioGuardarDto.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo.");
        });

        // Verificar que el rol exista
        Rol rol = rolRepository.findById(usuarioGuardarDto.getIdRol())
                .orElseThrow(() -> new NoSuchElementException("Rol no encontrado con id: " + usuarioGuardarDto.getIdRol()));

        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioGuardarDto.getEmail());
        usuario.setContrasena(usuarioGuardarDto.getContrasena());
        usuario.setRol(rol);
        usuario.setEstaActivo(usuarioGuardarDto.getEstaActivo());
        // fechas: se pueden asignar en el servicio (formato string usado en la entidad)
        usuario.setFechaCreacion(java.time.LocalDateTime.now().toString());

        Usuario guardado = usuarioRepository.save(usuario);
        return mapToDto(guardado);
    }

    @Override
    public UsuarioSalidaDto editar(UsuarioModificarDto usuarioModificarDto) {
        Usuario existente = usuarioRepository.findById(usuarioModificarDto.getId())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + usuarioModificarDto.getId()));

        // Si se cambia email, verificar duplicado
        if (usuarioModificarDto.getEmail() != null && !usuarioModificarDto.getEmail().equalsIgnoreCase(existente.getEmail())) {
            usuarioRepository.findByEmailIgnoreCase(usuarioModificarDto.getEmail())
                    .filter(u -> !u.getId().equals(usuarioModificarDto.getId()))
                    .ifPresent(u -> { throw new IllegalArgumentException("Otro usuario con ese correo ya existe."); });
            existente.setEmail(usuarioModificarDto.getEmail());
        }

        if (usuarioModificarDto.getContrasena() != null && !usuarioModificarDto.getContrasena().isBlank()) {
            existente.setContrasena(usuarioModificarDto.getContrasena());
        }

        // Actualizar rol
        Rol rol = rolRepository.findById(usuarioModificarDto.getIdRol())
                .orElseThrow(() -> new NoSuchElementException("Rol no encontrado con id: " + usuarioModificarDto.getIdRol()));
        existente.setRol(rol);

        existente.setEstaActivo(usuarioModificarDto.getEstaActivo());
        existente.setFechaActualizacion(java.time.LocalDateTime.now().toString());

        Usuario actualizado = usuarioRepository.save(existente);
        return mapToDto(actualizado);
    }

    @Override
    public void eliminarPorId(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NoSuchElementException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioSalidaDto> obtenerUsuariosPaginadosYFiltrados(Optional<String> busqueda, Pageable pageable) {
        Page<Usuario> page;
        if (busqueda == null || busqueda.isEmpty() || busqueda.orElse("").isBlank()) {
            page = usuarioRepository.findAll(pageable);
        } else {
            String q = busqueda.get().trim();
            // intentar parsear q a boolean para búsquedas por estaActivo
            Boolean activo = null;
            if (q.equalsIgnoreCase("true") || q.equalsIgnoreCase("false")) {
                activo = Boolean.valueOf(q);
                page = usuarioRepository.findByEstaActivo(activo, pageable);
            } else {
                page = usuarioRepository.findByEmailContainingIgnoreCase(q, pageable);
            }
        }
        return page.map(this::mapToDto);
    }
}
