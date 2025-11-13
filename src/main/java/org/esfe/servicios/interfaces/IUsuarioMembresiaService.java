package org.esfe.servicios.interfaces;

import org.esfe.dtos.usuarioMembresias.UsuarioMembresiaGuardarDto;
import org.esfe.dtos.usuarioMembresias.UsuarioMembresiaModificarDto;
import org.esfe.dtos.usuarioMembresias.UsuarioMembresiaSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUsuarioMembresiaService {

    List<UsuarioMembresiaSalidaDto> obtenerTodos();

    Optional<UsuarioMembresiaSalidaDto> obtenerPorId(Integer id);

    UsuarioMembresiaSalidaDto crear(UsuarioMembresiaGuardarDto dto);

    UsuarioMembresiaSalidaDto editar(UsuarioMembresiaModificarDto dto);

    void eliminarPorId(Integer id);

    Page<UsuarioMembresiaSalidaDto> obtenerPaginados(Pageable pageable);

    List<UsuarioMembresiaSalidaDto> obtenerPorUsuario(Integer usuarioId);
}

