package org.esfe.servicios.interfaces;

import org.esfe.dtos.usuario.PerfilDTO;
import org.esfe.dtos.usuario.PerfilCreateDTO;

import java.util.List;

public interface IPerfilService {
    List<PerfilDTO> listarTodos();
    PerfilDTO obtenerPorId(Integer id);
    PerfilDTO crear(PerfilCreateDTO dto);
    PerfilDTO actualizar(Integer id, PerfilCreateDTO dto);
    void eliminar(Integer id);
    PerfilDTO obtenerPorUsuarioId(Integer usuarioId);
    List<PerfilDTO> buscarPorCiudad(String ciudad);
    List<PerfilDTO> buscarPorPaisYCiudad(String pais, String ciudad);
}
