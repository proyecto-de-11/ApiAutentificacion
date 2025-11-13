package org.esfe.controladores;

import org.esfe.dtos.usuarioMembresias.UsuarioMembresiaGuardarDto;
import org.esfe.dtos.usuarioMembresias.UsuarioMembresiaModificarDto;
import org.esfe.dtos.usuarioMembresias.UsuarioMembresiaSalidaDto;
import org.esfe.servicios.interfaces.IUsuarioMembresiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/usuario-membresias")
public class UsuarioMembresiaController {

    @Autowired
    private IUsuarioMembresiaService usuarioMembresiaService;

    @GetMapping
    public ResponseEntity<Page<UsuarioMembresiaSalidaDto>> mostrarTodosPaginados(Pageable pageable) {
        Page<UsuarioMembresiaSalidaDto> page = usuarioMembresiaService.obtenerPaginados(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<UsuarioMembresiaSalidaDto>> mostrarTodos() {
        List<UsuarioMembresiaSalidaDto> lista = usuarioMembresiaService.obtenerTodos();
        if (!lista.isEmpty()) return ResponseEntity.ok(lista);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioMembresiaSalidaDto> buscarPorId(@PathVariable Integer id) {
        return usuarioMembresiaService.obtenerPorId(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody UsuarioMembresiaGuardarDto dto) {
        try {
            UsuarioMembresiaSalidaDto nuevo = usuarioMembresiaService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la relación: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id, @Valid @RequestBody UsuarioMembresiaModificarDto dto) {
        try {
            dto.setId(id);
            UsuarioMembresiaSalidaDto actualizado = usuarioMembresiaService.editar(dto);
            return ResponseEntity.ok(actualizado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la relación: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            usuarioMembresiaService.eliminarPorId(id);
            return ResponseEntity.ok("UsuarioMembresia eliminada correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la relación: " + e.getMessage());
        }
    }
}


