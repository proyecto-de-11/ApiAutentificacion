package org.esfe.configuracion.seguridad;

import org.esfe.modelos.Perfil;
import org.esfe.modelos.Usuario;
import org.esfe.repositorios.IPerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Helper para validar permisos de acceso a perfiles
 * Ahora lanza excepciones apropiadas en lugar de retornar false
 */
@Component("perfilSecurity")
public class PerfilSecurityHelper {

    @Autowired
    private IPerfilRepository perfilRepository;

    /**
     * Verifica si el usuario autenticado es el propietario del perfil
     * @param perfilId ID del perfil
     * @param authentication Objeto de autenticación
     * @return true si es el propietario
     * @throws AccessDeniedException si no es el propietario
     */
    public boolean isOwner(Integer perfilId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Debes iniciar sesión para acceder a este recurso");
        }

        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            Optional<Perfil> perfilOpt = perfilRepository.findById(perfilId);

            if (perfilOpt.isEmpty()) {
                throw new AccessDeniedException("El perfil solicitado no existe");
            }

            Perfil perfil = perfilOpt.get();

            if (perfil.getUsuario() == null || !perfil.getUsuario().getId().equals(usuario.getId())) {
                throw new AccessDeniedException("No tienes permiso para acceder a este perfil");
            }

            return true;
        } catch (ClassCastException e) {
            throw new AccessDeniedException("Error de autenticación: token inválido");
        }
    }

    /**
     * Verifica si el usuario puede acceder al perfil (es suyo o es ADMIN)
     * @param perfilId ID del perfil
     * @param authentication Objeto de autenticación
     * @return true si puede acceder
     * @throws AccessDeniedException si no tiene permisos
     */
    public boolean canAccess(Integer perfilId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Debes iniciar sesión para acceder a este recurso");
        }

        // Los ADMIN siempre pueden acceder
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            return true;
        }

        // Verificar si es el propietario (esto lanzará excepción si no lo es)
        return isOwner(perfilId, authentication);
    }
}