package org.esfe.configuracion.seguridad;

import org.esfe.modelos.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Utilidades de seguridad generales
 */
@Component
public class SecurityUtils {

    /**
     * Obtiene el usuario actual autenticado
     */
    public static Optional<Usuario> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            return Optional.of(usuario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Obtiene el ID del usuario actual
     */
    public static Optional<Integer> getCurrentUserId() {
        return getCurrentUser().map(Usuario::getId);
    }

    /**
     * Verifica si el usuario actual es ADMIN
     */
    public static boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));
    }

    /**
     * Verifica si el usuario actual tiene un rol específico
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;

        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(roleWithPrefix));
    }

    /**
     * Verifica si el usuario actual es el propietario del recurso
     */
    public static boolean isOwnerOrAdmin(Integer resourceOwnerId) {
        if (isCurrentUserAdmin()) {
            return true;
        }

        Optional<Integer> currentUserId = getCurrentUserId();
        return currentUserId.isPresent() && currentUserId.get().equals(resourceOwnerId);
    }
}