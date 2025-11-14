package org.esfe.servicios.interfaces;

import org.esfe.dtos.usuarioMembresias.PreferenciaUsuarioGuardarDTO;
import org.esfe.dtos.usuarioMembresias.PreferenciaUsuarioModificarDTO;
import org.esfe.dtos.usuarioMembresias.PreferenciaUsuarioSalidaDTO;

import java.util.List;

public interface IPreferenciaUsuarioService {
    PreferenciaUsuarioSalidaDTO guardar(PreferenciaUsuarioGuardarDTO dto);
    PreferenciaUsuarioSalidaDTO modificar(PreferenciaUsuarioModificarDTO dto);
    PreferenciaUsuarioSalidaDTO obtenerPorId(Integer id);
    PreferenciaUsuarioSalidaDTO obtenerPorUsuarioId(Integer usuarioId);
    List<PreferenciaUsuarioSalidaDTO> obtenerTodos();
    void eliminar(Integer id);
}

