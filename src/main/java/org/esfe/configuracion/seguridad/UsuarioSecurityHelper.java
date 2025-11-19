package org.esfe.configuracion.seguridad;

import org.esfe.modelos.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Helper para validar permisos de acceso a usuarios
 * Utilizado en las anotaciones @PreAuthorize de los controladores
 */
@Component("usuarioSecurity")
public class UsuarioSecurityHelper {

    /**
     * Verifica si el usuario autenticado es el propietario del recurso (mismo usuario)
     *
     * @param usuarioId      ID del usuario a verificar
     * @param authentication Objeto de autenticación de Spring Security
     * @return true si es el mismo usuario, false en caso contrario
     */
    public boolean isOwner(Integer usuarioId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            return usuario.getId().equals(usuarioId);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el usuario puede acceder al recurso
     * ADMIN puede acceder a todos
     * PROPIETARIO y USUARIO solo pueden acceder a su propia información
     */
    public boolean canAccess(Integer usuarioId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Los ADMIN siempre pueden acceder a cualquier usuario
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            return true;
        }

        // ✅ PROPIETARIO y USUARIO solo pueden acceder a SU PROPIO usuario
        return isOwner(usuarioId, authentication);
    }

    /**
     * Verifica si el usuario puede modificar el recurso
     * ADMIN puede modificar cualquier usuario
     * PROPIETARIO y USUARIO solo pueden modificarse a sí mismos
     */
    public boolean canModify(Integer usuarioId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Los ADMIN pueden modificar a cualquier usuario
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            return true;
        }

        // ✅ PROPIETARIO y USUARIO solo pueden modificarse a sí mismos
        return isOwner(usuarioId, authentication);
    }
}