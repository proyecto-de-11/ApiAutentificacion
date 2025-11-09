package org.esfe.controladores;

import org.esfe.dtos.tiposdeporte.TiposDeporteGuardarDto;
import org.esfe.dtos.tiposdeporte.TiposDeporteModificarDto;
import org.esfe.dtos.tiposdeporte.TiposDeporteSalidaDto;
import org.esfe.servicios.interfaces.ITipoDeporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/tiposdeporte")
public class TipoDeporteController {

    @Autowired
    private ITipoDeporteService tipoDeporteService;

    @GetMapping
    public ResponseEntity<Page<TiposDeporteSalidaDto>> mostrarTodosPaginadosYFiltrados(
            @RequestParam(required = false) Optional<String> busqueda,
            Pageable pageable) {

        Page<TiposDeporteSalidaDto> tiposDeportePage = 
            tipoDeporteService.obtenerTiposDeportePaginadosYFiltrados(busqueda, pageable);

        if (tiposDeportePage.hasContent()) {
            return ResponseEntity.ok(tiposDeportePage);
        }
        
        return ResponseEntity.ok(tiposDeportePage);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<TiposDeporteSalidaDto>> mostrarTodos() {
        List<TiposDeporteSalidaDto> tiposDeporte = tipoDeporteService.obtenerTodos();
        if (!tiposDeporte.isEmpty()) {
            return ResponseEntity.ok(tiposDeporte);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TiposDeporteSalidaDto> buscarPorId(@PathVariable Long id) {
        return tipoDeporteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody TiposDeporteGuardarDto tipoDeporteGuardarDto) {
        try {
            TiposDeporteSalidaDto nuevoTipo = tipoDeporteService.crear(tipoDeporteGuardarDto);
            // Retorna 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipo);
        } catch (IllegalArgumentException e) {
            // Captura errores de validación de negocio (ej. nombre duplicado)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el Tipo de Deporte: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody TiposDeporteModificarDto tipoDeporteModificarDto) {
        try {
            // Asegurar que el ID del path coincida con el ID del body
            tipoDeporteModificarDto.setId(id); 
            TiposDeporteSalidaDto actualizado = tipoDeporteService.editar(tipoDeporteModificarDto);
            return ResponseEntity.ok(actualizado);
        } catch (NoSuchElementException e) {
            // No encontrado (ID incorrecto)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
             // Conflicto (Nombre duplicado)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // Otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try {
            tipoDeporteService.eliminarPorId(id);
            return ResponseEntity.ok("Tipo de Deporte eliminado correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar: " + e.getMessage());
        }
    }
}