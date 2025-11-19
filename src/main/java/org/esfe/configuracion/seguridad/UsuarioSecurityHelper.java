package org.esfe.configuracion.seguridad;

import org.esfe.modelos.Usuario;
import org.springframework.security.access.AccessDeniedException;
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
     * @return true si es el mismo usuario
     * @throws AccessDeniedException si no es el propietario
     */
    public boolean isOwner(Integer usuarioId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Debes iniciar sesión para acceder a este recurso");
        }

        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();

            if (!usuario.getId().equals(usuarioId)) {
                throw new AccessDeniedException("No tienes permiso para acceder a la información de este usuario");
            }

            return true;
        } catch (ClassCastException e) {
            throw new AccessDeniedException("Error de autenticación: token inválido");
        }
    }

    /**
     * Verifica si el usuario puede acceder al recurso
     * ADMIN puede acceder a todos
     * PROPIETARIO y USUARIO solo pueden acceder a su propia información
     *
     * @param usuarioId ID del usuario
     * @param authentication Objeto de autenticación
     * @return true si puede acceder
     * @throws AccessDeniedException si no tiene permisos
     */
    public boolean canAccess(Integer usuarioId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Debes iniciar sesión para acceder a este recurso");
        }

        // Los ADMIN siempre pueden acceder a cualquier usuario
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            return true;
        }

        // PROPIETARIO y USUARIO solo pueden acceder a SU PROPIO usuario
        return isOwner(usuarioId, authentication);
    }

    /**
     * Verifica si el usuario puede modificar el recurso
     * ADMIN puede modificar cualquier usuario
     * PROPIETARIO y USUARIO solo pueden modificarse a sí mismos
     *
     * @param usuarioId ID del usuario
     * @param authentication Objeto de autenticación
     * @return true si puede modificar
     * @throws AccessDeniedException si no tiene permisos
     */
    public boolean canModify(Integer usuarioId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Debes iniciar sesión para realizar esta acción");
        }

        // Los ADMIN pueden modificar a cualquier usuario
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            return true;
        }

        // PROPIETARIO y USUARIO solo pueden modificarse a sí mismos
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();

            if (!usuario.getId().equals(usuarioId)) {
                throw new AccessDeniedException("No tienes permiso para modificar la información de este usuario");
            }

            return true;
        } catch (ClassCastException e) {
            throw new AccessDeniedException("Error de autenticación: token inválido");
        }
    }
}