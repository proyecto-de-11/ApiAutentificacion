package org.esfe.servicios.implementaciones;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public PreferenciaUsuarioService(IPreferenciaUsuarioRepository preferenciaRepo,
                                     IUsuarioRepository usuarioRepo,
                                     ITiposDeporteRepository tipoDeporteRepo) {
        this.preferenciaRepo = preferenciaRepo;
        this.usuarioRepo = usuarioRepo;
        this.tipoDeporteRepo = tipoDeporteRepo;
        this.objectMapper = new ObjectMapper();
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

        // Validar que todos los tipos de deporte existan
        for (Long tipoId : dto.getTiposDeporteIds()) {
            if (!tipoDeporteRepo.existsById(tipoId)) {
                throw new IllegalArgumentException("Tipo de deporte no encontrado con ID: " + tipoId);
            }
        }

        PreferenciaUsuario ent = new PreferenciaUsuario();
        ent.setUsuario(usuario);

        // Convertir List<Long> a JSON string
        try {
            String idsJson = objectMapper.writeValueAsString(dto.getTiposDeporteIds());
            ent.setTipoDeporteIds(idsJson);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error al procesar los IDs de tipos de deporte", e);
        }

        mapDtoToEntity(dto, ent);

        PreferenciaUsuario saved = preferenciaRepo.save(ent);
        return mapEntityToSalida(saved);
    }

    @Override
    public PreferenciaUsuarioSalidaDTO modificar(PreferenciaUsuarioModificarDTO dto) {
        PreferenciaUsuario ent = preferenciaRepo.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Preferencia no encontrada"));

        // Si se proporcionan nuevos tipos de deporte
        if (dto.getTiposDeporteIds() != null) {
            if (dto.getTiposDeporteIds().isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar al menos un tipo de deporte");
            }

            // Validar que todos los tipos de deporte existan
            for (Long tipoId : dto.getTiposDeporteIds()) {
                if (!tipoDeporteRepo.existsById(tipoId)) {
                    throw new IllegalArgumentException("Tipo de deporte no encontrado con ID: " + tipoId);
                }
            }

            // Convertir List<Long> a JSON string
            try {
                String idsJson = objectMapper.writeValueAsString(dto.getTiposDeporteIds());
                ent.setTipoDeporteIds(idsJson);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Error al procesar los IDs de tipos de deporte", e);
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

        // Convertir JSON string a List<Long> y luego buscar los TipoDeporte
        if (e.getTipoDeporteIds() != null && !e.getTipoDeporteIds().isEmpty()) {
            try {
                List<Long> ids = objectMapper.readValue(e.getTipoDeporteIds(), new TypeReference<List<Long>>(){});

                // Buscar cada TipoDeporte por su ID
                List<PreferenciaUsuarioSalidaDTO.TipoDeporteSimpleDTO> tiposDto = new ArrayList<>();
                for (Long id : ids) {
                    Optional<TipoDeporte> tdOpt = tipoDeporteRepo.findById(id);
                    if (tdOpt.isPresent()) {
                        TipoDeporte td = tdOpt.get();
                        tiposDto.add(new PreferenciaUsuarioSalidaDTO.TipoDeporteSimpleDTO(
                                td.getId(),
                                td.getNombre(),
                                td.getIcono()
                        ));
                    }
                }
                s.setTiposDeporte(tiposDto);

            } catch (JsonProcessingException ex) {
                // En caso de error, retornar lista vacía
                s.setTiposDeporte(new ArrayList<>());
            }
        }

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

    // Mapeo de DTO a Entity (solo campos comunes, no tiposDeporteIds)
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

