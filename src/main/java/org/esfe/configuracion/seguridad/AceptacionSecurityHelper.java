package org.esfe.configuracion.seguridad;

import org.esfe.modelos.Usuario;
import org.esfe.modelos.UsuarioAceptacionTermino;
import org.esfe.repositorios.IUsuarioAceptacionTerminoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Helper para validar permisos de acceso a aceptaciones de términos
 */
@Component("aceptacionSecurity")
public class AceptacionSecurityHelper {

    @Autowired
    private IUsuarioAceptacionTerminoRepository aceptacionRepository;

    /**
     * Verifica si el usuario autenticado es el propietario de la aceptación
     */
    public boolean isOwner(Integer aceptacionId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            Optional<UsuarioAceptacionTermino> aceptacionOpt = aceptacionRepository.findById(aceptacionId);

            if (aceptacionOpt.isEmpty()) {
                return false;
            }

            UsuarioAceptacionTermino aceptacion = aceptacionOpt.get();
            return aceptacion.getUsuario() != null &&
                    aceptacion.getUsuario().getId().equals(usuario.getId());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el usuario puede acceder a la aceptación
     */
    public boolean canAccess(Integer aceptacionId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            return true;
        }

        return isOwner(aceptacionId, authentication);
    }
}