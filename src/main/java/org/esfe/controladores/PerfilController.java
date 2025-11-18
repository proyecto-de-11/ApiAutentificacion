package org.esfe.controladores;

import org.esfe.dtos.usuario.PerfilSalidaDto;
import org.esfe.dtos.usuario.PerfilGuardarDto;
import org.esfe.dtos.usuario.PerfilModificarDto;
import org.esfe.servicios.interfaces.IPerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/perfiles")
@Validated
public class PerfilController {

    @Autowired
    private IPerfilService perfilService;

    // ✅ Listar todos: Solo ADMIN y si esta autenticado
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public List<PerfilSalidaDto> listarTodos() {
        return perfilService.listarTodos();
    }

    // ✅ Ver por ID: ADMIN o el mismo usuario (incluyendo PROPIETARIO)
    @PreAuthorize("hasRole('ADMINISTRADOR') or @perfilSecurity.isOwner(#id, authentication)")
    @GetMapping("/{id}")
    public PerfilSalidaDto obtenerPorId(@PathVariable Integer id) {
        return perfilService.obtenerPorId(id);
    }

    // ✅ Crear: Usuario autenticado crea su propio perfil (PROPIETARIO incluido)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROPIETARIO', 'USUARIO')")
    @PostMapping
    public ResponseEntity<PerfilSalidaDto> crear(@Valid @RequestBody PerfilGuardarDto dto,
                                                 Authentication authentication) {
        // Verificar que el usuario solo pueda crear su propio perfil
        org.esfe.modelos.Usuario usuario = (org.esfe.modelos.Usuario) authentication.getPrincipal();

        // ADMIN puede crear perfiles para cualquiera, otros solo para sí mismos
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (!isAdmin && !dto.getUsuarioId().equals(usuario.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        PerfilSalidaDto created = perfilService.crear(dto);
        return ResponseEntity.created(URI.create("/perfiles/" + created.getId())).body(created);
    }

    // ✅ Actualizar: ADMIN o el mismo usuario (PROPIETARIO incluido)
    @PreAuthorize("hasRole('ADMINISTRADOR') or @perfilSecurity.isOwner(#id, authentication)")
    @PutMapping("/{id}")
    public PerfilSalidaDto actualizar(@PathVariable Integer id,
                                      @Valid @RequestBody PerfilModificarDto dto) {
        return perfilService.actualizar(id, dto);
    }

    // ✅ Eliminar: Solo ADMIN (PROPIETARIO NO puede eliminar)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        perfilService.eliminar(id);
    }

    // ✅ Obtener por usuario: ADMIN o el mismo usuario
    @PreAuthorize("hasRole('ADMINISTRADOR') or #usuarioId == authentication.principal.id")
    @GetMapping("/usuario/{usuarioId}")
    public PerfilSalidaDto obtenerPorUsuarioId(@PathVariable Integer usuarioId) {
        return perfilService.obtenerPorUsuarioId(usuarioId);
    }

    // ✅ Búsquedas: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/ciudad/{ciudad}")
    public List<PerfilSalidaDto> buscarPorCiudad(@PathVariable String ciudad) {
        return perfilService.buscarPorCiudad(ciudad);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/buscar")
    public List<PerfilSalidaDto> buscarPorPaisYCiudad(@RequestParam String pais, @RequestParam String ciudad) {
        return perfilService.buscarPorPaisYCiudad(pais, ciudad);
    }
}