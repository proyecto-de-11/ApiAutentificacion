package org.esfe.servicios.interfaces;

import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoGuardarDto;
import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoModificarDto;
import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUsuarioAceptacionTerminoService {

    List<UsuarioAceptacionTerminoSalidaDto> obtenerTodos();

    Optional<UsuarioAceptacionTerminoSalidaDto> obtenerPorId(Integer id);

    UsuarioAceptacionTerminoSalidaDto crear(UsuarioAceptacionTerminoGuardarDto dto);

    UsuarioAceptacionTerminoSalidaDto editar(UsuarioAceptacionTerminoModificarDto dto);

    void eliminarPorId(Integer id);

    Page<UsuarioAceptacionTerminoSalidaDto> obtenerPaginadoYFiltrado(Optional<Integer> idUsuario, Optional<Integer> idDocumentoLegal, Pageable pageable);
}

