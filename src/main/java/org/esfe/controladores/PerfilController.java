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

    // ✅ Listar todos: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public List<PerfilSalidaDto> listarTodos() {
        return perfilService.listarTodos();
    }

    // ✅ Ver por ID: ADMIN o el mismo usuario
    @PreAuthorize("hasRole('ADMINISTRADOR') or @perfilSecurity.isOwner(#id, authentication)")
    @GetMapping("/{id}")
    public PerfilSalidaDto obtenerPorId(@PathVariable Integer id) {
        return perfilService.obtenerPorId(id);
    }

    // ✅ Crear: Usuario autenticado crea su propio perfil
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<PerfilSalidaDto> crear(@Valid @RequestBody PerfilGuardarDto dto,
                                                 Authentication authentication) {
        // Verificar que el usuario solo pueda crear su propio perfil
        org.esfe.modelos.Usuario usuario = (org.esfe.modelos.Usuario) authentication.getPrincipal();
        if (!dto.getUsuarioId().equals(usuario.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        PerfilSalidaDto created = perfilService.crear(dto);
        return ResponseEntity.created(URI.create("/perfiles/" + created.getId())).body(created);
    }

    // ✅ Actualizar: ADMIN o el mismo usuario
    @PreAuthorize("hasRole('ADMINISTRADOR') or @perfilSecurity.isOwner(#id, authentication)")
    @PutMapping("/{id}")
    public PerfilSalidaDto actualizar(@PathVariable Integer id,
                                      @Valid @RequestBody PerfilModificarDto dto) {
        return perfilService.actualizar(id, dto);
    }

    // ✅ Eliminar: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        perfilService.eliminar(id);
    }

    // ✅ Obtener por usuario: ADMIN o el mismo usuario
    // or #usuarioId == authentication.principal.id"
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/usuario/{usuarioId}")
    public PerfilSalidaDto obtenerPorUsuarioId(@PathVariable Integer usuarioId) {
        return perfilService.obtenerPorUsuarioId(usuarioId);
    }

    // ✅ Obtener por usuario: ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/ciudad/{ciudad}")
    public List<PerfilSalidaDto> buscarPorCiudad(@PathVariable String ciudad) {
        return perfilService.buscarPorCiudad(ciudad);
    }

    // ✅ Obtener por usuario: ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/buscar")
    public List<PerfilSalidaDto> buscarPorPaisYCiudad(@RequestParam String pais, @RequestParam String ciudad) {
        return perfilService.buscarPorPaisYCiudad(pais, ciudad);
    }
}