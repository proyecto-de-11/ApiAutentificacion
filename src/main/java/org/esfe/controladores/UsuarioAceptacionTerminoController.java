package org.esfe.controladores;

import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoGuardarDto;
import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoModificarDto;
import org.esfe.dtos.usuarioAceptacionTermino.UsuarioAceptacionTerminoSalidaDto;
import org.esfe.servicios.interfaces.IUsuarioAceptacionTerminoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
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

    // ✅ Listar todos: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<UsuarioAceptacionTerminoSalidaDto>> listarTodos() {
        List<UsuarioAceptacionTerminoSalidaDto> lista = service.obtenerTodos();
        return ResponseEntity.ok(lista);
    }

    // ✅ Obtener por ID: ADMIN o el mismo usuario (PROPIETARIO incluido)
    @PreAuthorize("hasRole('ADMINISTRADOR') or @aceptacionSecurity.canAccess(#id, authentication)")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioAceptacionTerminoSalidaDto> obtenerPorId(@PathVariable Integer id) {
        Optional<UsuarioAceptacionTerminoSalidaDto> opt = service.obtenerPorId(id);
        return opt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ✅ Crear: PROPIETARIO y USUARIO pueden crear sus propias aceptaciones
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROPIETARIO', 'USUARIO')")
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody UsuarioAceptacionTerminoGuardarDto dto,
                                   HttpServletRequest request,
                                   Authentication authentication) {
        try {
            // Verificar que el usuario solo pueda crear sus propias aceptaciones
            org.esfe.modelos.Usuario usuario = (org.esfe.modelos.Usuario) authentication.getPrincipal();

            // ADMIN puede crear para cualquiera, otros solo para sí mismos
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

            if (!isAdmin && !dto.getIdUsuario().equals(usuario.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No puedes crear aceptaciones para otro usuario");
            }

            // Capturar IP automáticamente
            String ipAddress = getClientIP(request);
            dto.setIpAddress(ipAddress);

            UsuarioAceptacionTerminoSalidaDto creado = service.crear(dto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(creado.getId())
                    .toUri();
            return ResponseEntity.created(location).body(creado);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    // ✅ Editar: ADMIN o el mismo usuario (PROPIETARIO incluido)
    @PreAuthorize("hasRole('ADMINISTRADOR') or @aceptacionSecurity.canAccess(#id, authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id,
                                    @Valid @RequestBody UsuarioAceptacionTerminoModificarDto dto,
                                    HttpServletRequest request) {
        try {
            if (dto.getId() == null) dto.setId(id);
            else if (!dto.getId().equals(id))
                return ResponseEntity.badRequest().body("El ID en la ruta no coincide con el ID en el cuerpo.");

            // Capturar IP automáticamente
            String ipAddress = getClientIP(request);
            dto.setIpAddress(ipAddress);

            UsuarioAceptacionTerminoSalidaDto actualizado = service.editar(dto);
            return ResponseEntity.ok(actualizado);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    // ✅ Eliminar: Solo ADMIN (PROPIETARIO NO puede eliminar)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            service.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // ✅ Paginado y filtrado: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/paginado")
    public ResponseEntity<Page<UsuarioAceptacionTerminoSalidaDto>> paginado(
            @RequestParam(name = "idUsuario", required = false) Integer idUsuario,
            @RequestParam(name = "idDocumentoLegal", required = false) Integer idDocumentoLegal,
            Pageable pageable) {

        Page<UsuarioAceptacionTerminoSalidaDto> page = service.obtenerPaginadoYFiltrado(
                Optional.ofNullable(idUsuario),
                Optional.ofNullable(idDocumentoLegal),
                pageable);
        return ResponseEntity.ok(page);
    }

    // Método helper para obtener la IP del cliente
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || "unknown".equalsIgnoreCase(xfHeader)) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0].trim();
    }
}