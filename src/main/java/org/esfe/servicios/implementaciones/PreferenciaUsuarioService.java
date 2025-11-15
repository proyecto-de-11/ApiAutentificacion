package org.esfe.servicios.implementaciones;

import org.esfe.dtos.usuarioMembresias.PreferenciaUsuarioGuardarDTO;
import org.esfe.dtos.usuarioMembresias.PreferenciaUsuarioModificarDTO;
import org.esfe.dtos.usuarioMembresias.PreferenciaUsuarioSalidaDTO;
import org.esfe.modelos.PreferenciaUsuario;
import org.esfe.modelos.TipoDeporte;
import org.esfe.modelos.Usuario;
import org.esfe.repositorios.IPreferenciaUsuarioRepository;
import org.esfe.repositorios.ITiposDeporteRepository;
import org.esfe.repositorios.IUsuarioRepository;
import org.esfe.servicios.interfaces.IPreferenciaUsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PreferenciaUsuarioService implements IPreferenciaUsuarioService {

    private final IPreferenciaUsuarioRepository preferenciaRepo;
    private final IUsuarioRepository usuarioRepo;
    private final ITiposDeporteRepository tipoDeporteRepo;

    public PreferenciaUsuarioService(IPreferenciaUsuarioRepository preferenciaRepo,
                                     IUsuarioRepository usuarioRepo,
                                     ITiposDeporteRepository tipoDeporteRepo) {
        this.preferenciaRepo = preferenciaRepo;
        this.usuarioRepo = usuarioRepo;
        this.tipoDeporteRepo = tipoDeporteRepo;
    }

    @Override
    public PreferenciaUsuarioSalidaDTO guardar(PreferenciaUsuarioGuardarDTO dto) {
        // Validar que el usuario exista
        Usuario usuario = usuarioRepo.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar que el usuario no tenga ya una preferencia (OneToOne)
        Optional<PreferenciaUsuario> existente = preferenciaRepo.findByUsuarioId(dto.getUsuarioId());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("El usuario ya tiene preferencias configuradas. Use el método de actualización.");
        }

        // Validar que se hayan proporcionado tipos de deporte
        if (dto.getTiposDeporteIds() == null || dto.getTiposDeporteIds().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un tipo de deporte");
        }

        PreferenciaUsuario ent = new PreferenciaUsuario();
        ent.setUsuario(usuario);

        // Agregar los tipos de deporte
        for (Long tipoId : dto.getTiposDeporteIds()) {
            TipoDeporte tipo = tipoDeporteRepo.findById(tipoId)
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de deporte no encontrado con ID: " + tipoId));
            ent.addTipoDeporte(tipo);
        }

        mapDtoToEntity(dto, ent);

        PreferenciaUsuario saved = preferenciaRepo.save(ent);
        return mapEntityToSalida(saved);
    }

    @Override
    public PreferenciaUsuarioSalidaDTO modificar(PreferenciaUsuarioModificarDTO dto) {
        PreferenciaUsuario ent = preferenciaRepo.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Preferencia no encontrada"));

        // Si se proporcionan nuevos tipos de deporte, reemplazar los existentes
        if (dto.getTiposDeporteIds() != null) {
            if (dto.getTiposDeporteIds().isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar al menos un tipo de deporte");
            }

            // Limpiar los tipos de deporte existentes
            ent.clearTiposDeporte();

            // Agregar los nuevos tipos de deporte
            for (Long tipoId : dto.getTiposDeporteIds()) {
                TipoDeporte tipo = tipoDeporteRepo.findById(tipoId)
                        .orElseThrow(() -> new IllegalArgumentException("Tipo de deporte no encontrado con ID: " + tipoId));
                ent.addTipoDeporte(tipo);
            }
        }

        mapDtoToEntity(dto, ent);

        PreferenciaUsuario saved = preferenciaRepo.save(ent);
        return mapEntityToSalida(saved);
    }

    @Override
    public PreferenciaUsuarioSalidaDTO obtenerPorId(Integer id) {
        return preferenciaRepo.findById(id)
                .map(this::mapEntityToSalida)
                .orElseThrow(() -> new IllegalArgumentException("Preferencia no encontrada"));
    }

    @Override
    public PreferenciaUsuarioSalidaDTO obtenerPorUsuarioId(Integer usuarioId) {
        return preferenciaRepo.findByUsuarioId(usuarioId)
                .map(this::mapEntityToSalida)
                .orElseThrow(() -> new IllegalArgumentException("Preferencia no encontrada para usuario"));
    }

    @Override
    public List<PreferenciaUsuarioSalidaDTO> obtenerTodos() {
        return preferenciaRepo.findAll()
                .stream()
                .map(this::mapEntityToSalida)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Integer id) {
        if (!preferenciaRepo.existsById(id)) {
            throw new IllegalArgumentException("Preferencia no encontrada");
        }
        preferenciaRepo.deleteById(id);
    }

    // Mapeo de Entity a DTO
    private PreferenciaUsuarioSalidaDTO mapEntityToSalida(PreferenciaUsuario e) {
        PreferenciaUsuarioSalidaDTO s = new PreferenciaUsuarioSalidaDTO();
        s.setId(e.getId());

        if (e.getUsuario() != null) {
            s.setUsuarioId(e.getUsuario().getId());
        }

        // Mapear tipos de deporte desde la relación Many-to-Many
        List<PreferenciaUsuarioSalidaDTO.TipoDeporteSimpleDTO> tiposDto = e.getTiposDeporte().stream()
                .map(rel -> {
                    TipoDeporte td = rel.getTipoDeporte();
                    return new PreferenciaUsuarioSalidaDTO.TipoDeporteSimpleDTO(
                            td.getId(),
                            td.getNombre(),
                            td.getIcono()
                    );
                })
                .collect(Collectors.toList());
        s.setTiposDeporte(tiposDto);

        s.setNivelJuego(e.getNivelJuego());
        s.setPosicionPreferida(e.getPosicionPreferida());
        s.setHorarioPreferidoInicio(e.getHorarioPreferidoInicio());
        s.setHorarioPreferidoFin(e.getHorarioPreferidoFin());
        s.setDiasPreferidos(e.getDiasPreferidos());
        s.setCiudadPreferida(e.getCiudadPreferida());
        s.setRadiosDistanciaKm(e.getRadiosDistanciaKm());
        s.setNotificacionesEmail(e.getNotificacionesEmail());
        s.setNotificacionesPush(e.getNotificacionesPush());
        s.setNotificacionesPartidos(e.getNotificacionesPartidos());
        s.setNotificacionesTorneos(e.getNotificacionesTorneos());
        s.setNotificacionesInvitaciones(e.getNotificacionesInvitaciones());
        s.setFechaGuardado(e.getFechaGuardado());
        s.setFechaActualizado(e.getFechaActualizado());

        return s;
    }

    // Mapeo de DTO a Entity (solo campos comunes)
    private void mapDtoToEntity(Object dtoObj, PreferenciaUsuario ent) {
        if (dtoObj instanceof PreferenciaUsuarioGuardarDTO) {
            PreferenciaUsuarioGuardarDTO dto = (PreferenciaUsuarioGuardarDTO) dtoObj;
            ent.setNivelJuego(dto.getNivelJuego());
            ent.setPosicionPreferida(dto.getPosicionPreferida());
            ent.setHorarioPreferidoInicio(dto.getHorarioPreferidoInicio());
            ent.setHorarioPreferidoFin(dto.getHorarioPreferidoFin());
            ent.setDiasPreferidos(dto.getDiasPreferidos());
            ent.setCiudadPreferida(dto.getCiudadPreferida());
            ent.setRadiosDistanciaKm(dto.getRadiosDistanciaKm());
            ent.setNotificacionesEmail(dto.getNotificacionesEmail());
            ent.setNotificacionesPush(dto.getNotificacionesPush());
            ent.setNotificacionesPartidos(dto.getNotificacionesPartidos());
            ent.setNotificacionesTorneos(dto.getNotificacionesTorneos());
            ent.setNotificacionesInvitaciones(dto.getNotificacionesInvitaciones());
        } else if (dtoObj instanceof PreferenciaUsuarioModificarDTO) {
            PreferenciaUsuarioModificarDTO dto = (PreferenciaUsuarioModificarDTO) dtoObj;
            if (dto.getNivelJuego() != null) ent.setNivelJuego(dto.getNivelJuego());
            if (dto.getPosicionPreferida() != null) ent.setPosicionPreferida(dto.getPosicionPreferida());
            if (dto.getHorarioPreferidoInicio() != null) ent.setHorarioPreferidoInicio(dto.getHorarioPreferidoInicio());
            if (dto.getHorarioPreferidoFin() != null) ent.setHorarioPreferidoFin(dto.getHorarioPreferidoFin());
            if (dto.getDiasPreferidos() != null) ent.setDiasPreferidos(dto.getDiasPreferidos());
            if (dto.getCiudadPreferida() != null) ent.setCiudadPreferida(dto.getCiudadPreferida());
            if (dto.getRadiosDistanciaKm() != null) ent.setRadiosDistanciaKm(dto.getRadiosDistanciaKm());
            if (dto.getNotificacionesEmail() != null) ent.setNotificacionesEmail(dto.getNotificacionesEmail());
            if (dto.getNotificacionesPush() != null) ent.setNotificacionesPush(dto.getNotificacionesPush());
            if (dto.getNotificacionesPartidos() != null) ent.setNotificacionesPartidos(dto.getNotificacionesPartidos());
            if (dto.getNotificacionesTorneos() != null) ent.setNotificacionesTorneos(dto.getNotificacionesTorneos());
            if (dto.getNotificacionesInvitaciones() != null) ent.setNotificacionesInvitaciones(dto.getNotificacionesInvitaciones());
        }
    }
}
