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
import java.util.stream.Collectors;

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

    // ✅ NUEVO: Listar perfiles públicos (solo datos básicos) - Todos los usuarios autenticados
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/publicos")
    public ResponseEntity<List<PerfilPublicoDto>> listarPerfilesPublicos() {
        List<PerfilSalidaDto> todosLosPerfiles = perfilService.listarTodos();

        // Mapear a DTO público con solo los campos solicitados
        List<PerfilPublicoDto> perfilesPublicos = todosLosPerfiles.stream()
                .map(this::mapearAPerfilPublico)
                .collect(Collectors.toList());

        return ResponseEntity.ok(perfilesPublicos);
    }

    // ✅ NUEVO: Obtener perfil público por ID - Todos los usuarios autenticados
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/publicos/{id}")
    public ResponseEntity<PerfilPublicoDto> obtenerPerfilPublicoPorId(@PathVariable Integer id) {
        PerfilSalidaDto perfil = perfilService.obtenerPorId(id);
        PerfilPublicoDto perfilPublico = mapearAPerfilPublico(perfil);
        return ResponseEntity.ok(perfilPublico);
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

    // ===== MÉTODO AUXILIAR PARA MAPEO =====

    /**
     * Método auxiliar para mapear PerfilSalidaDto a PerfilPublicoDto
     * Centraliza la lógica de conversión para reutilización
     */
    private PerfilPublicoDto mapearAPerfilPublico(PerfilSalidaDto perfil) {
        PerfilPublicoDto dto = new PerfilPublicoDto();
        // Obtener usuarioId del objeto usuario
        if (perfil.getUsuario() != null) {
            dto.setUsuarioId(perfil.getUsuario().getId());
        }
        dto.setNombreCompleto(perfil.getNombreCompleto());
        dto.setFotoPerfil(perfil.getFotoPerfil());
        dto.setBiografia(perfil.getBiografia());
        return dto;
    }

    // ===== CLASE INTERNA: DTO PARA PERFILES PÚBLICOS =====

    /**
     * DTO simplificado para mostrar información pública de perfiles
     * Solo contiene los campos básicos visibles para todos los usuarios autenticados
     */
    public static class PerfilPublicoDto {
        private Integer usuarioId;
        private String nombreCompleto;
        private String fotoPerfil;
        private String biografia;

        // Getters y Setters
        public Integer getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(Integer usuarioId) {
            this.usuarioId = usuarioId;
        }

        public String getNombreCompleto() {
            return nombreCompleto;
        }

        public void setNombreCompleto(String nombreCompleto) {
            this.nombreCompleto = nombreCompleto;
        }

        public String getFotoPerfil() {
            return fotoPerfil;
        }

        public void setFotoPerfil(String fotoPerfil) {
            this.fotoPerfil = fotoPerfil;
        }

        public String getBiografia() {
            return biografia;
        }

        public void setBiografia(String biografia) {
            this.biografia = biografia;
        }
    }
}