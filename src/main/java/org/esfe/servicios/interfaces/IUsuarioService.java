package org.esfe.servicios.interfaces;

import org.esfe.dtos.usuario.UsuarioGuardarDto;
import org.esfe.dtos.usuario.UsuarioModificarDto;
import org.esfe.dtos.usuario.UsuarioSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<UsuarioSalidaDto> obtenerTodos();

    Optional<UsuarioSalidaDto> obtenerPorId(Integer id);

    UsuarioSalidaDto crear(UsuarioGuardarDto usuarioGuardarDto);

    UsuarioSalidaDto editar(UsuarioModificarDto usuarioModificarDto);

    void eliminarPorId(Integer id);

    Page<UsuarioSalidaDto> obtenerUsuariosPaginadosYFiltrados(Optional<String> busqueda, Pageable pageable);
}

