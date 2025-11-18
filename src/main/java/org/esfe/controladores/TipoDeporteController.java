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
import org.springframework.security.access.prepost.PreAuthorize;
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

    // ✅ Ver lista paginada: Todos los usuarios autenticados
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<TiposDeporteSalidaDto>> mostrarTodosPaginadosYFiltrados(
            @RequestParam(required = false) Optional<String> busqueda,
            Pageable pageable) {

        Page<TiposDeporteSalidaDto> tiposDeportePage =
                tipoDeporteService.obtenerTiposDeportePaginadosYFiltrados(busqueda, pageable);

        return ResponseEntity.ok(tiposDeportePage);
    }

    // ✅ Ver lista completa: Todos los usuarios autenticados
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/lista")
    public ResponseEntity<List<TiposDeporteSalidaDto>> mostrarTodos() {
        List<TiposDeporteSalidaDto> tiposDeporte = tipoDeporteService.obtenerTodos();
        if (!tiposDeporte.isEmpty()) {
            return ResponseEntity.ok(tiposDeporte);
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ Ver por ID: Todos los usuarios autenticados
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<TiposDeporteSalidaDto> buscarPorId(@PathVariable Long id) {
        return tipoDeporteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Crear: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody TiposDeporteGuardarDto tipoDeporteGuardarDto) {
        try {
            TiposDeporteSalidaDto nuevoTipo = tipoDeporteService.crear(tipoDeporteGuardarDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el Tipo de Deporte: " + e.getMessage());
        }
    }

    // ✅ Editar: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody TiposDeporteModificarDto tipoDeporteModificarDto) {
        try {
            tipoDeporteModificarDto.setId(id);
            TiposDeporteSalidaDto actualizado = tipoDeporteService.editar(tipoDeporteModificarDto);
            return ResponseEntity.ok(actualizado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar: " + e.getMessage());
        }
    }

    // ✅ Eliminar: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try {
            tipoDeporteService.eliminarPorId(id);
            return ResponseEntity.ok("Tipo de Deporte eliminado correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar: " + e.getMessage());
        }
    }
}