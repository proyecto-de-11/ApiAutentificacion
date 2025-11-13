package org.esfe.servicios.implementaciones;

import org.esfe.dtos.membresias.MembresiaGuardarDto;
import org.esfe.dtos.membresias.MembresiaModificarDto;
import org.esfe.dtos.membresias.MembresiaSalidaDto;
import org.esfe.modelos.Membresia;
import org.esfe.repositorios.IMembresiaRepository;
import org.esfe.servicios.interfaces.IMembresiaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MembresiaService implements IMembresiaService {

    @Autowired
    private IMembresiaRepository membresiaRepository;

    @Autowired
    private ModelMapper modelMapper;

    private MembresiaSalidaDto mapToDto(Membresia m) {
        return modelMapper.map(m, MembresiaSalidaDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembresiaSalidaDto> obtenerTodos() {
        return membresiaRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MembresiaSalidaDto> obtenerPorId(Integer id) {
        return membresiaRepository.findById(id).map(this::mapToDto);
    }

    @Override
    public MembresiaSalidaDto crear(MembresiaGuardarDto dto) {
        if (membresiaRepository.existsByNombreAndPrecioMensual(dto.getNombre(), dto.getPrecioMensual())) {
            throw new IllegalArgumentException("Ya existe una membresía con el nombre '" + dto.getNombre() + "' y el mismo precio.");
        }

        Membresia m = modelMapper.map(dto, Membresia.class);
        m.setId(null);
        m.setFechaCreacion(LocalDateTime.now());

        Membresia guardada = membresiaRepository.save(m);
        return mapToDto(guardada);
    }

    @Override
    public MembresiaSalidaDto editar(MembresiaModificarDto dto) {
        Membresia existente = membresiaRepository.findById(dto.getId())
                .orElseThrow(() -> new NoSuchElementException("Membresía no encontrada con id: " + dto.getId()));

        boolean otroConMismoNombreYPrecio = membresiaRepository.existsByNombreAndPrecioMensual(dto.getNombre(), dto.getPrecioMensual());
        if (otroConMismoNombreYPrecio && (existente.getNombre() == null || !existente.getNombre().equals(dto.getNombre()) ||
                existente.getPrecioMensual() == null || existente.getPrecioMensual().compareTo(dto.getPrecioMensual()) != 0)) {
            throw new IllegalArgumentException("Otra membresía ya existe con el nombre '" + dto.getNombre() + "' y el mismo precio.");
        }

        modelMapper.map(dto, existente);

        Membresia actualizado = membresiaRepository.save(existente);
        return mapToDto(actualizado);
    }

    @Override
    public void eliminarPorId(Integer id) {
        if (!membresiaRepository.existsById(id)) {
            throw new NoSuchElementException("Membresía no encontrada con id: " + id);
        }
        membresiaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MembresiaSalidaDto> obtenerMembresiasPaginadasYFiltradas(Optional<String> busqueda, Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
        }

        Page<Membresia> page;
        String textoBusqueda = busqueda.orElse("").trim();

        if (!textoBusqueda.isBlank()) {
            page = membresiaRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(textoBusqueda, textoBusqueda, pageable);
        } else {
            page = membresiaRepository.findAll(pageable);
        }

        List<MembresiaSalidaDto> dtoList = page.stream().map(this::mapToDto).collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombreYPrecioMensual(String nombre, BigDecimal precioMensual) {
        return membresiaRepository.existsByNombreAndPrecioMensual(nombre, precioMensual);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembresiaSalidaDto> buscarPorEstaActivo(Boolean estaActivo) {
        return membresiaRepository.findByEstaActivo(estaActivo).stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
