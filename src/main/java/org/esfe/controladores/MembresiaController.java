package org.esfe.controladores;

import org.esfe.dtos.membresias.MembresiaGuardarDto;
import org.esfe.dtos.membresias.MembresiaModificarDto;
import org.esfe.dtos.membresias.MembresiaSalidaDto;
import org.esfe.servicios.interfaces.IMembresiaService;
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
@RequestMapping("/api/membresias")
public class MembresiaController {

    @Autowired
    private IMembresiaService membresiaService;

    // ✅ Ver lista paginada: Todos los usuarios autenticados
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<MembresiaSalidaDto>> mostrarTodosPaginadosYFiltrados(
            @RequestParam(required = false) Optional<String> busqueda,
            Pageable pageable) {

        Page<MembresiaSalidaDto> page = membresiaService.obtenerMembresiasPaginadasYFiltradas(busqueda, pageable);
        return ResponseEntity.ok(page);
    }

    // ✅ Ver lista completa: Todos los usuarios autenticados
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/lista")
    public ResponseEntity<List<MembresiaSalidaDto>> mostrarTodos() {
        List<MembresiaSalidaDto> lista = membresiaService.obtenerTodos();
        if (!lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ Ver por ID: Todos los usuarios autenticados
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<MembresiaSalidaDto> buscarPorId(@PathVariable Integer id) {
        return membresiaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Crear: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody MembresiaGuardarDto dto) {
        try {
            MembresiaSalidaDto nuevo = membresiaService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la membresía: " + e.getMessage());
        }
    }

    // ✅ Editar: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id, @Valid @RequestBody MembresiaModificarDto dto) {
        try {
            dto.setId(id);
            MembresiaSalidaDto actualizado = membresiaService.editar(dto);
            return ResponseEntity.ok(actualizado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la membresía: " + e.getMessage());
        }
    }

    // ✅ Eliminar: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            membresiaService.eliminarPorId(id);
            return ResponseEntity.ok("Membresía eliminada correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la membresía: " + e.getMessage());
        }
    }
}

