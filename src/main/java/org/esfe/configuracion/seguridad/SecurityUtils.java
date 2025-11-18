package org.esfe.configuracion.seguridad;

import org.esfe.modelos.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
        return hasRole("ADMINISTRADOR");
    }

    /**
     * ✅ NUEVO: Verifica si el usuario actual es PROPIETARIO
     */
    public static boolean isCurrentUserPropietario() {
        return hasRole("PROPIETARIO");
    }

    /**
     * ✅ NUEVO: Verifica si el usuario tiene rol privilegiado (ADMIN o PROPIETARIO)
     */
    public static boolean isPrivilegedUser() {
        return hasAnyRole("ADMINISTRADOR", "PROPIETARIO");
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
     * ✅ NUEVO: Verifica si tiene alguno de los roles especificados
     */
    public static boolean hasAnyRole(String... roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        for (String role : roles) {
            String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
            boolean hasRole = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals(roleWithPrefix));
            if (hasRole) {
                return true;
            }
        }
        return false;
    }

    /**
     * ✅ ACTUALIZADO: Verifica si es propietario o admin
     */
    public static boolean isOwnerOrPrivileged(Integer resourceOwnerId) {
        // Si es ADMIN o PROPIETARIO, permitir acceso
        if (isPrivilegedUser()) {
            return true;
        }

        // Si es USUARIO normal, verificar propiedad
        Optional<Integer> currentUserId = getCurrentUserId();
        return currentUserId.isPresent() && currentUserId.get().equals(resourceOwnerId);
    }

    /**
     * ✅ NUEVO: Verifica si puede modificar un recurso
     * ADMIN puede modificar todo
     * PROPIETARIO y USUARIO solo sus propios recursos
     */
    public static boolean canModifyResource(Integer resourceOwnerId) {
        if (isCurrentUserAdmin()) {
            return true; // ADMIN puede modificar TODO
        }

        // PROPIETARIO y USUARIO solo pueden modificar lo suyo
        Optional<Integer> currentUserId = getCurrentUserId();
        return currentUserId.isPresent() && currentUserId.get().equals(resourceOwnerId);
    }

    /**
     * ✅ NUEVO: Verifica si puede eliminar un recurso
     * Solo ADMIN y PROPIETARIO pueden eliminar
     */
    public static boolean canDeleteResource(Integer resourceOwnerId) {
        if (!isPrivilegedUser()) {
            return false; // USUARIO normal no puede eliminar
        }

        if (isCurrentUserAdmin()) {
            return true; // ADMIN puede eliminar TODO
        }

        // PROPIETARIO solo puede eliminar lo suyo
        Optional<Integer> currentUserId = getCurrentUserId();
        return currentUserId.isPresent() && currentUserId.get().equals(resourceOwnerId);
    }
}