package org.esfe.servicios.implementaciones;

import org.esfe.dtos.usuario.PerfilSalidaDto;
import org.esfe.dtos.usuario.PerfilGuardarDto;
import org.esfe.dtos.usuario.PerfilModificarDto;
import org.esfe.modelos.Perfil;
import org.esfe.modelos.Usuario;
import org.esfe.repositorios.IPerfilRepository;
import org.esfe.repositorios.IUsuarioRepository;
import org.esfe.servicios.interfaces.IPerfilService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PerfilService implements IPerfilService {

    @Autowired
    private IPerfilRepository perfilRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PerfilSalidaDto> listarTodos() {
        return perfilRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PerfilSalidaDto obtenerPorId(Integer id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil no encontrado"));
        return convertToDto(perfil);
    }

    @Override
    public PerfilSalidaDto crear(PerfilGuardarDto dto) {
        // Verificar que no exista ya un perfil para ese usuario (one-to-one)
        if (perfilRepository.existsByUsuario_Id(dto.getUsuarioId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya tiene un perfil");
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        Perfil perfil = modelMapper.map(dto, Perfil.class);
        perfil.setUsuario(usuario);
        Perfil saved = perfilRepository.save(perfil);
        return convertToDto(saved);
    }

    @Override
    public PerfilSalidaDto actualizar(Integer id, PerfilModificarDto dto) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil no encontrado"));

        // mapear campos no nulos del DTO al entity
        if (dto.getNombreCompleto() != null) perfil.setNombreCompleto(dto.getNombreCompleto());
        if (dto.getTelefono() != null) perfil.setTelefono(dto.getTelefono());
        if (dto.getDocumentoIdentidad() != null) perfil.setDocumentoIdentidad(dto.getDocumentoIdentidad());
        if (dto.getFechaNacimiento() != null) perfil.setFechaNacimiento(dto.getFechaNacimiento());
        if (dto.getGenero() != null) perfil.setGenero(dto.getGenero());
        if (dto.getFotoPerfil() != null) perfil.setFotoPerfil(dto.getFotoPerfil());
        if (dto.getBiografia() != null) perfil.setBiografia(dto.getBiografia());
        if (dto.getCiudad() != null) perfil.setCiudad(dto.getCiudad());
        if (dto.getPais() != null) perfil.setPais(dto.getPais());

        if (dto.getUsuarioId() != null) {
            // Si se intenta cambiar el usuario asociado, verificar que no tenga ya un perfil distinto
            Integer usuarioActualId = (perfil.getUsuario() != null) ? perfil.getUsuario().getId() : null;
            if (perfilRepository.existsByUsuario_Id(dto.getUsuarioId()) && (usuarioActualId == null || !usuarioActualId.equals(dto.getUsuarioId()))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario destino ya tiene un perfil");
            }
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
            perfil.setUsuario(usuario);
        }

        Perfil updated = perfilRepository.save(perfil);
        return convertToDto(updated);
    }

    @Override
    public void eliminar(Integer id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil no encontrado"));
        perfilRepository.delete(perfil);
    }

    @Override
    public PerfilSalidaDto obtenerPorUsuarioId(Integer usuarioId) {
        Perfil perfil = perfilRepository.findByUsuario_Id(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil para usuario no encontrado"));
        return convertToDto(perfil);
    }

    @Override
    public List<PerfilSalidaDto> buscarPorCiudad(String ciudad) {
        return perfilRepository.findByCiudadIgnoreCase(ciudad)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PerfilSalidaDto> buscarPorPaisYCiudad(String pais, String ciudad) {
        return perfilRepository.buscarPorPaisYCiudad(pais, ciudad)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PerfilSalidaDto convertToDto(Perfil perfil) {
        PerfilSalidaDto dto = modelMapper.map(perfil, PerfilSalidaDto.class);
        if (perfil.getUsuario() != null) {
            dto.setUsuarioId(perfil.getUsuario().getId());
        }
        return dto;
    }
}
