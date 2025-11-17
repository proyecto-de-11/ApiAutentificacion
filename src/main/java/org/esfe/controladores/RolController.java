package org.esfe.controladores;

import org.esfe.dtos.rol.RolGuardarDto;
import org.esfe.dtos.rol.RolModificarDto;
import org.esfe.dtos.rol.RolSalidaDto;
import org.esfe.servicios.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@Validated
public class RolController {

    @Autowired
    private IRolService rolService;

    // ✅ Listar todos: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<RolSalidaDto>> listarTodos() {
        List<RolSalidaDto> lista = rolService.obtenerTodos();
        return ResponseEntity.ok(lista);
    }

    // ✅ Obtener por ID: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<RolSalidaDto> obtenerPorId(@PathVariable Long id) {
        Optional<RolSalidaDto> opt = rolService.obtenerPorId(id);
        return opt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ✅ Crear: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody RolGuardarDto dto) {
        try {
            RolSalidaDto creado = rolService.crear(dto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(creado.getId())
                    .toUri();
            return ResponseEntity.created(location).body(creado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ✅ Editar: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody RolModificarDto dto) {
        try {
            if (dto.getId() == null) {
                dto.setId(id);
            } else if (!dto.getId().equals(id)) {
                return ResponseEntity.badRequest().body("El ID en la ruta no coincide con el ID en el cuerpo.");
            }

            RolSalidaDto actualizado = rolService.editar(dto);
            return ResponseEntity.ok(actualizado);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ✅ Eliminar: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            rolService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // ✅ Paginado: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/paginado")
    public ResponseEntity<Page<RolSalidaDto>> paginado(
            @RequestParam(name = "busqueda", required = false) String busqueda,
            Pageable pageable) {
        Page<RolSalidaDto> page = rolService.obtenerRolesPaginadosYFiltrados(
                Optional.ofNullable(busqueda), pageable);
        return ResponseEntity.ok(page);
    }
}
