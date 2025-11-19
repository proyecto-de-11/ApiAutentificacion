package org.esfe.controladores;

import org.esfe.dtos.usuario.UsuarioGuardarDto;
import org.esfe.dtos.usuario.UsuarioModificarDto;
import org.esfe.dtos.usuario.UsuarioSalidaDto;
import org.esfe.servicios.interfaces.IUsuarioService;
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
@RequestMapping("/api/usuarios")
@Validated
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    // ✅ Solo ADMIN puede listar todos los usuarios
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<UsuarioSalidaDto>> listarTodos() {
        List<UsuarioSalidaDto> lista = usuarioService.obtenerTodos();
        return ResponseEntity.ok(lista);
    }

    // ✅ Solo ADMIN puede ver cualquier usuario
    // Un usuario normal solo debería poder ver su propio perfil
    @PreAuthorize("hasRole('ADMINISTRADOR') or @usuarioSecurity.isOwner(#id, authentication)")
    @GetMapping("/{id}")
    public Optional<UsuarioSalidaDto> obtenerPorId(@PathVariable Integer id) {
        /*Optional<UsuarioSalidaDto> opt = usuarioService.obtenerPorId(id);
        return opt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());*/
        return usuarioService.obtenerPorId(id);
    }

    // ✅ Crear usuario: público (se hace en registro) o solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody UsuarioGuardarDto dto) {
        try {
            UsuarioSalidaDto creado = usuarioService.crear(dto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(creado.getId())
                    .toUri();
            return ResponseEntity.created(location).body(creado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ✅ Editar: Solo ADMIN o el mismo usuario
    @PreAuthorize("hasRole('ADMINISTRADOR') or @usuarioSecurity.isOwner(#id, authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id, @Valid @RequestBody UsuarioModificarDto dto) {
        try {
            if (dto.getId() == null) {
                dto.setId(id);
            } else if (!dto.getId().equals(id)) {
                return ResponseEntity.badRequest().body("El ID en la ruta no coincide con el ID en el cuerpo.");
            }

            UsuarioSalidaDto actualizado = usuarioService.editar(dto);
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
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            usuarioService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // ✅ Paginado: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/paginado")
    public ResponseEntity<Page<UsuarioSalidaDto>> paginado(
            @RequestParam(name = "busqueda", required = false) String busqueda,
            Pageable pageable) {
        Page<UsuarioSalidaDto> page = usuarioService.obtenerUsuariosPaginadosYFiltrados(
                Optional.ofNullable(busqueda), pageable);
        return ResponseEntity.ok(page);
    }
}
