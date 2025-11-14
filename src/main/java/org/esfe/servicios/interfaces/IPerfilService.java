package org.esfe.servicios.interfaces;

import org.esfe.dtos.usuario.PerfilSalidaDto;
import org.esfe.dtos.usuario.PerfilGuardarDto;
import org.esfe.dtos.usuario.PerfilModificarDto;

import java.util.List;

public interface IPerfilService {
    List<PerfilSalidaDto> listarTodos();
    PerfilSalidaDto obtenerPorId(Integer id);
    PerfilSalidaDto crear(PerfilGuardarDto dto);
    PerfilSalidaDto actualizar(Integer id, PerfilModificarDto dto);
    void eliminar(Integer id);
    PerfilSalidaDto obtenerPorUsuarioId(Integer usuarioId);
    List<PerfilSalidaDto> buscarPorCiudad(String ciudad);
    List<PerfilSalidaDto> buscarPorPaisYCiudad(String pais, String ciudad);
}
