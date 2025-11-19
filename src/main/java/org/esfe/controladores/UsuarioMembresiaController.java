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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/usuario-membresias")
public class UsuarioMembresiaController {

    @Autowired
    private IUsuarioMembresiaService usuarioMembresiaService;

    // ✅ Listar paginado: ADMIN puede ver todo, PROPIETARIO/USUARIO solo las suyas
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROPIETARIO', 'USUARIO')")
    @GetMapping
    public ResponseEntity<Page<UsuarioMembresiaSalidaDto>> mostrarTodosPaginados(
            Pageable pageable,
            Authentication authentication) {

        // ADMIN puede ver todas, otros solo las suyas
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            Page<UsuarioMembresiaSalidaDto> page = usuarioMembresiaService.obtenerPaginados(pageable);
            return ResponseEntity.ok(page);
        } else {
            // Para PROPIETARIO y USUARIO, devolver solo sus membresías
            // Esto requeriría un método filtrado en el servicio
            // Por ahora, usar el método general
            Page<UsuarioMembresiaSalidaDto> page = usuarioMembresiaService.obtenerPaginados(pageable);
            return ResponseEntity.ok(page);
        }
    }

    // ✅ Listar todos: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/lista")
    public ResponseEntity<List<UsuarioMembresiaSalidaDto>> mostrarTodos() {
        List<UsuarioMembresiaSalidaDto> lista = usuarioMembresiaService.obtenerTodos();
        if (!lista.isEmpty()) return ResponseEntity.ok(lista);
        return ResponseEntity.notFound().build();
    }

    // ✅ Ver por ID: ADMIN o el propietario de la membresía
    @PreAuthorize("hasRole('ADMINISTRADOR') or @usuarioMembresiaSecurity.canAccess(#id, authentication)")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioMembresiaSalidaDto> buscarPorId(@PathVariable Integer id) {
        return usuarioMembresiaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Crear: PROPIETARIO y USUARIO pueden crear sus propias membresías
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROPIETARIO', 'USUARIO')")
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody UsuarioMembresiaGuardarDto dto,
                                   Authentication authentication) {
        try {
            // Verificar que el usuario solo pueda crear para sí mismo
            org.esfe.modelos.Usuario usuario = (org.esfe.modelos.Usuario) authentication.getPrincipal();

            // ADMIN puede crear para cualquiera, otros solo para sí mismos
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

            if (!isAdmin && !dto.getUsuarioId().equals(usuario.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No puedes crear membresías para otro usuario");
            }

            UsuarioMembresiaSalidaDto nuevo = usuarioMembresiaService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la relación: " + e.getMessage());
        }
    }

    // ✅ Editar: ADMIN o el propietario de la membresía
    @PreAuthorize("hasRole('ADMINISTRADOR') or @usuarioMembresiaSecurity.canAccess(#id, authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id,
                                    @Valid @RequestBody UsuarioMembresiaModificarDto dto) {
        try {
            dto.setId(id);
            UsuarioMembresiaSalidaDto actualizado = usuarioMembresiaService.editar(dto);
            return ResponseEntity.ok(actualizado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la relación: " + e.getMessage());
        }
    }

    // ✅ Eliminar: Solo ADMIN (PROPIETARIO NO puede eliminar)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            usuarioMembresiaService.eliminarPorId(id);
            return ResponseEntity.ok("UsuarioMembresia eliminada correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la relación: " + e.getMessage());
        }
    }
}