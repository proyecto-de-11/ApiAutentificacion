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
     * @param usuarioId ID del usuario a verificar
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
     * Los ADMIN siempre pueden acceder, otros usuarios solo pueden acceder a su propia información
     * @param usuarioId ID del usuario a verificar
     * @param authentication Objeto de autenticación
     * @return true si puede acceder, false en caso contrario
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

        // Los demás usuarios solo pueden acceder a su propia información
        return isOwner(usuarioId, authentication);
    }

    /**
     * Verifica si el usuario puede modificar el recurso
     * Los ADMIN pueden modificar cualquier usuario, otros solo pueden modificarse a sí mismos
     * @param usuarioId ID del usuario a modificar
     * @param authentication Objeto de autenticación
     * @return true si puede modificar, false en caso contrario
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

        // Los usuarios normales solo pueden modificarse a sí mismos
        return isOwner(usuarioId, authentication);
    }

    /**
     * Verifica si el usuario puede eliminar el recurso
     * Solo los ADMIN pueden eliminar usuarios
     * @param usuarioId ID del usuario a eliminar
     * @param authentication Objeto de autenticación
     * @return true si puede eliminar, false en caso contrario
     */
    public boolean canDelete(Integer usuarioId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Solo ADMIN puede eliminar usuarios
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));
    }

    /**
     * Verifica si el usuario autenticado es ADMIN
     * @param authentication Objeto de autenticación
     * @return true si es ADMIN, false en caso contrario
     */
    public boolean isAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));
    }

    /**
     * Verifica si el usuario autenticado es PROPIETARIO
     * @param authentication Objeto de autenticación
     * @return true si es PROPIETARIO, false en caso contrario
     */
    public boolean isPropietario(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PROPIETARIO"));
    }

    /**
     * Verifica si el usuario tiene privilegios (ADMIN o PROPIETARIO)
     * @param authentication Objeto de autenticación
     * @return true si tiene privilegios, false en caso contrario
     */
    public boolean hasPrivilegedRole(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(auth ->
                        auth.getAuthority().equals("ROLE_ADMINISTRADOR") ||
                                auth.getAuthority().equals("ROLE_PROPIETARIO")
                );
    }
}