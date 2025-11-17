package org.esfe.configuracion.seguridad;

import org.esfe.modelos.Perfil;
import org.esfe.modelos.Usuario;
import org.esfe.repositorios.IPerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Helper para validar permisos de acceso a perfiles
 */
@Component("perfilSecurity")
public class PerfilSecurityHelper {

    @Autowired
    private IPerfilRepository perfilRepository;

    /**
     * Verifica si el usuario autenticado es el propietario del perfil
     * @ param perfilId ID del perfil
     * @ param authentication Objeto de autenticación
     * @ return true si es el propietario, false en caso contrario
     */
    public boolean isOwner(Integer perfilId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            Optional<Perfil> perfilOpt = perfilRepository.findById(perfilId);

            if (perfilOpt.isEmpty()) {
                return false;
            }

            Perfil perfil = perfilOpt.get();
            return perfil.getUsuario() != null &&
                    perfil.getUsuario().getId().equals(usuario.getId());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el usuario puede acceder al perfil (es suyo o es ADMIN)
     */
    public boolean canAccess(Integer perfilId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Los ADMIN siempre pueden acceder
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            return true;
        }

        // Verificar si es el propietario
        return isOwner(perfilId, authentication);
    }
}