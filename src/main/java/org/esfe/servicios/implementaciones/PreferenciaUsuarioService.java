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
        PreferenciaUsuario ent = new PreferenciaUsuario();

        // usuario
        Optional<Usuario> u = usuarioRepo.findById(dto.getUsuarioId());
        if (u.isEmpty()) throw new IllegalArgumentException("Usuario no encontrado");
        ent.setUsuario(u.get());

        // tipo deporte
        if (dto.getTipoDeporteId() != null) {
            Optional<TipoDeporte> td = tipoDeporteRepo.findById(dto.getTipoDeporteId());
            td.ifPresent(ent::setTipoDeporte);
        }

        mapDtoToEntity(dto, ent);

        PreferenciaUsuario saved = preferenciaRepo.save(ent);
        return mapEntityToSalida(saved);
    }

    @Override
    public PreferenciaUsuarioSalidaDTO modificar(PreferenciaUsuarioModificarDTO dto) {
        Optional<PreferenciaUsuario> opt = preferenciaRepo.findById(dto.getId());
        if (opt.isEmpty()) throw new IllegalArgumentException("Preferencia no encontrada");
        PreferenciaUsuario ent = opt.get();

        if (dto.getTipoDeporteId() != null) {
            Optional<TipoDeporte> td = tipoDeporteRepo.findById(dto.getTipoDeporteId());
            td.ifPresent(ent::setTipoDeporte);
        }

        mapDtoToEntity(dto, ent);
        PreferenciaUsuario saved = preferenciaRepo.save(ent);
        return mapEntityToSalida(saved);
    }

    @Override
    public PreferenciaUsuarioSalidaDTO obtenerPorId(Integer id) {
        return preferenciaRepo.findById(id).map(this::mapEntityToSalida)
                .orElseThrow(() -> new IllegalArgumentException("Preferencia no encontrada"));
    }

    @Override
    public PreferenciaUsuarioSalidaDTO obtenerPorUsuarioId(Integer usuarioId) {
        return preferenciaRepo.findByUsuarioId(usuarioId).map(this::mapEntityToSalida)
                .orElseThrow(() -> new IllegalArgumentException("Preferencia no encontrada para usuario"));
    }

    @Override
    public List<PreferenciaUsuarioSalidaDTO> obtenerTodos() {
        return preferenciaRepo.findAll().stream().map(this::mapEntityToSalida).collect(Collectors.toList());
    }

    @Override
    public void eliminar(Integer id) {
        preferenciaRepo.deleteById(id);
    }

    // Mapeos
    private PreferenciaUsuarioSalidaDTO mapEntityToSalida(PreferenciaUsuario e) {
        PreferenciaUsuarioSalidaDTO s = new PreferenciaUsuarioSalidaDTO();
        s.setId(e.getId());
        if (e.getUsuario() != null) s.setUsuarioId(e.getUsuario().getId());
        if (e.getTipoDeporte() != null) s.setTipoDeporteId(e.getTipoDeporte().getId());
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

    private void mapDtoToEntity(Object dtoObj, PreferenciaUsuario ent) {
        // support both guardar and modificar dtos via instanceof
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
        }
    }
}

