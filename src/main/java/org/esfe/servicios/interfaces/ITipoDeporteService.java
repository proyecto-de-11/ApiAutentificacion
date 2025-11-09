package org.esfe.servicios.interfaces;

import org.esfe.dtos.tiposdeporte.TiposDeporteGuardarDto;
import org.esfe.dtos.tiposdeporte.TiposDeporteModificarDto;
import org.esfe.dtos.tiposdeporte.TiposDeporteSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ITipoDeporteService {

    List<TiposDeporteSalidaDto> obtenerTodos();

    Optional<TiposDeporteSalidaDto> obtenerPorId(Long id); 

    TiposDeporteSalidaDto crear(TiposDeporteGuardarDto tipoDeporteGuardarDto);

    TiposDeporteSalidaDto editar(TiposDeporteModificarDto tipoDeporteModificarDto);

    void eliminarPorId(Long id);

    Page<TiposDeporteSalidaDto> obtenerTiposDeportePaginadosYFiltrados(Optional<String> busqueda, Pageable pageable);
}