package org.esfe.servicios.interfaces;

import org.esfe.dtos.membresias.MembresiaGuardarDto;
import org.esfe.dtos.membresias.MembresiaModificarDto;
import org.esfe.dtos.membresias.MembresiaSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IMembresiaService {

    List<MembresiaSalidaDto> obtenerTodos();

    Optional<MembresiaSalidaDto> obtenerPorId(Integer id);

    MembresiaSalidaDto crear(MembresiaGuardarDto dto);

    MembresiaSalidaDto editar(MembresiaModificarDto dto);

    void eliminarPorId(Integer id);

    Page<MembresiaSalidaDto> obtenerMembresiasPaginadasYFiltradas(Optional<String> busqueda, Pageable pageable);

    boolean existePorNombreYVersion(String nombre, String version);

    List<MembresiaSalidaDto> buscarPorFechaVigenciaAnteriorA(LocalDate fecha);

    List<MembresiaSalidaDto> buscarPorEstaActivo(Boolean estaActivo);
}

