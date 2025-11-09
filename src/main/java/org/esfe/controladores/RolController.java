package org.esfe.controladores;

import org.esfe.dtos.rol.RolGuardarDto;
import org.esfe.dtos.rol.RolModificarDto;
import org.esfe.dtos.rol.RolSalidaDto;
import org.esfe.servicios.interfaces.IRolService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final IRolService rolService;

    public RolController(IRolService rolService) {
        this.rolService = rolService;
    }

    // Listar todos (sin paginar)
    @GetMapping
    public ResponseEntity<List<RolSalidaDto>> listarTodos() {
        List<RolSalidaDto> lista = rolService.obtenerTodos();
        return ResponseEntity.ok(lista);
    }

    // Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<RolSalidaDto> obtenerPorId(@PathVariable Long id) {
        Optional<RolSalidaDto> opt = rolService.obtenerPorId(id);
        return opt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Crear
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

    // Editar (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody RolModificarDto dto) {
        try {
            // Asegurar coherencia del id
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

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            rolService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Paginado y filtrado
    @GetMapping("/paginado")
    public ResponseEntity<Page<RolSalidaDto>> paginado(
            @RequestParam(name = "busqueda", required = false) String busqueda,
            Pageable pageable) {
        Page<RolSalidaDto> page = rolService.obtenerRolesPaginadosYFiltrados(Optional.ofNullable(busqueda), pageable);
        return ResponseEntity.ok(page);
    }

}

