package org.esfe.servicios.interfaces;

import org.esfe.dtos.RolGuardarDto;
import org.esfe.dtos.RolModificarDto;
import org.esfe.dtos.RolSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IRolService {

    List<RolSalidaDto> obtenerTodos();

    Optional<RolSalidaDto> obtenerPorId(Long id); 

    RolSalidaDto crear(RolGuardarDto rolGuardarDto);

    RolSalidaDto editar(RolModificarDto rolModificarDto);

    void eliminarPorId(Long id);

    Page<RolSalidaDto> obtenerRolesPaginadosYFiltrados(Optional<String> busqueda, Pageable pageable);
}