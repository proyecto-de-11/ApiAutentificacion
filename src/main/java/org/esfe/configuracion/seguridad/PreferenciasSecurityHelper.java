package org.esfe.configuracion.seguridad;

import org.esfe.modelos.PreferenciaUsuario;
import org.esfe.modelos.Usuario;
import org.esfe.repositorios.IPreferenciaUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Helper para validar permisos de acceso a preferencias de usuario
 */
@Component("preferenciasSecurity")
public class PreferenciasSecurityHelper {

    @Autowired
    private IPreferenciaUsuarioRepository preferenciaRepository;

    /**
     * Verifica si el usuario autenticado es el propietario de las preferencias
     */
    public boolean isOwner(Integer preferenciaId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            Optional<PreferenciaUsuario> preferenciaOpt = preferenciaRepository.findById(preferenciaId);

            if (preferenciaOpt.isEmpty()) {
                return false;
            }

            PreferenciaUsuario preferencia = preferenciaOpt.get();
            return preferencia.getUsuario() != null &&
                    preferencia.getUsuario().getId().equals(usuario.getId());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el usuario puede acceder a las preferencias
     */
    public boolean canAccess(Integer preferenciaId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Los ADMIN siempre pueden acceder
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            return true;
        }

        return isOwner(preferenciaId, authentication);
    }
}