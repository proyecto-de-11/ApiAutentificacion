package org.esfe.servicios.implementaciones;

import org.esfe.dtos.tiposdeporte.TiposDeporteGuardarDto;
import org.esfe.dtos.tiposdeporte.TiposDeporteModificarDto;
import org.esfe.dtos.tiposdeporte.TiposDeporteSalidaDto;
import org.esfe.modelos.TipoDeporte;
import org.esfe.repositorios.ITiposDeporteRepository;
import org.esfe.servicios.interfaces.ITipoDeporteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TipoDeporteService implements ITipoDeporteService {

    @Autowired
    private ITiposDeporteRepository tiposDeporteRepository;

    @Autowired
    private ModelMapper modelMapper;

    private TiposDeporteSalidaDto mapToDto(TipoDeporte tipoDeporte) {
        return modelMapper.map(tipoDeporte, TiposDeporteSalidaDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TiposDeporteSalidaDto> obtenerTodos() {
        return tiposDeporteRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TiposDeporteSalidaDto> obtenerPorId(Long id) {
        return tiposDeporteRepository.findById(id).map(this::mapToDto);
    }

    @Override
    public TiposDeporteSalidaDto crear(TiposDeporteGuardarDto tipoDeporteGuardarDto) {
        tiposDeporteRepository.findByNombreIgnoreCase(tipoDeporteGuardarDto.getNombre()).ifPresent(td -> {
            throw new IllegalArgumentException("Ya existe un Tipo de Deporte con el nombre proporcionado: " + td.getNombre());
        });

        TipoDeporte tipoDeporte = modelMapper.map(tipoDeporteGuardarDto, TipoDeporte.class);
        tipoDeporte.setId(null); 
        TipoDeporte guardado = tiposDeporteRepository.save(tipoDeporte);

        return mapToDto(guardado);
    }

    @Override
    public TiposDeporteSalidaDto editar(TiposDeporteModificarDto tipoDeporteModificarDto) {
        TipoDeporte existente = tiposDeporteRepository.findById(tipoDeporteModificarDto.getId())
                .orElseThrow(() -> new NoSuchElementException("Tipo de Deporte no encontrado con id: " + tipoDeporteModificarDto.getId()));

        tiposDeporteRepository.findByNombreIgnoreCase(tipoDeporteModificarDto.getNombre())
                .filter(td -> !td.getId().equals(tipoDeporteModificarDto.getId()))
                .ifPresent(td -> { throw new IllegalArgumentException("Otro Tipo de Deporte con ese nombre ya existe: " + td.getNombre()); });

        modelMapper.map(tipoDeporteModificarDto, existente);

        TipoDeporte actualizado = tiposDeporteRepository.save(existente);
        
        return mapToDto(actualizado);
    }

    @Override
    public void eliminarPorId(Long id) {
        if (!tiposDeporteRepository.existsById(id)) {
            throw new NoSuchElementException("Tipo de Deporte no encontrado con id: " + id);
        }
        tiposDeporteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TiposDeporteSalidaDto> obtenerTiposDeportePaginadosYFiltrados(Optional<String> busqueda, Pageable pageable) {
        
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                pageable.getPageNumber(), 
                pageable.getPageSize(), 
                Sort.by("id").descending()
            );
        }

        Page<TipoDeporte> page;

        if (busqueda.isPresent() && !busqueda.get().isBlank()) {
            String q = busqueda.get().trim();
            page = tiposDeporteRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(q, q, pageable);
        } else {
            page = tiposDeporteRepository.findAll(pageable);
        }

        List<TiposDeporteSalidaDto> dtoList = page.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }
}