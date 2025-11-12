package org.esfe.controladores;

import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoGuardarDto;
import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoModificarDto;
import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoSalidaDto;
import org.esfe.servicios.interfaces.IUsuarioAceptacionTerminoService;
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
@RequestMapping("/api/aceptaciones")
@Validated
public class UsuarioAceptacionTerminoController {

    private final IUsuarioAceptacionTerminoService service;

    public UsuarioAceptacionTerminoController(IUsuarioAceptacionTerminoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioAceptacionTerminoSalidaDto>> listarTodos() {
        List<UsuarioAceptacionTerminoSalidaDto> lista = service.obtenerTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioAceptacionTerminoSalidaDto> obtenerPorId(@PathVariable Integer id) {
        Optional<UsuarioAceptacionTerminoSalidaDto> opt = service.obtenerPorId(id);
        return opt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody UsuarioAceptacionTerminoGuardarDto dto) {
        try {
            UsuarioAceptacionTerminoSalidaDto creado = service.crear(dto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(creado.getId())
                    .toUri();
            return ResponseEntity.created(location).body(creado);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id, @Valid @RequestBody UsuarioAceptacionTerminoModificarDto dto) {
        try {
            if (dto.getId() == null) dto.setId(id);
            else if (!dto.getId().equals(id)) return ResponseEntity.badRequest().body("El ID en la ruta no coincide con el ID en el cuerpo.");

            UsuarioAceptacionTerminoSalidaDto actualizado = service.editar(dto);
            return ResponseEntity.ok(actualizado);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            service.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<UsuarioAceptacionTerminoSalidaDto>> paginado(
            @RequestParam(name = "idUsuario", required = false) Integer idUsuario,
            @RequestParam(name = "idDocumentoLegal", required = false) Integer idDocumentoLegal,
            Pageable pageable) {

        Page<UsuarioAceptacionTerminoSalidaDto> page = service.obtenerPaginadoYFiltrado(Optional.ofNullable(idUsuario), Optional.ofNullable(idDocumentoLegal), pageable);
        return ResponseEntity.ok(page);
    }

}

