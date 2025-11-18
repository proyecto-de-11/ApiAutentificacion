package org.esfe.configuracion.seguridad;

import org.esfe.modelos.Usuario;
import org.esfe.modelos.UsuarioMembresia;
import org.esfe.repositorios.IUsuarioMembresiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Helper para validar permisos de acceso a membresías de usuario
 */
@Component("usuarioMembresiaSecurity")
public class UsuarioMembresiaSecurityHelper {

    @Autowired
    private IUsuarioMembresiaRepository usuarioMembresiaRepository;

    /**
     * Verifica si el usuario autenticado es el propietario de la membresía
     */
    public boolean isOwner(Integer usuarioMembresiaId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            Optional<UsuarioMembresia> membresiaOpt = usuarioMembresiaRepository.findById(usuarioMembresiaId);

            if (membresiaOpt.isEmpty()) {
                return false;
            }

            UsuarioMembresia membresia = membresiaOpt.get();
            return membresia.getUsuario() != null &&
                    membresia.getUsuario().getId().equals(usuario.getId());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el usuario puede acceder a la membresía
     */
    public boolean canAccess(Integer usuarioMembresiaId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            return true;
        }

        return isOwner(usuarioMembresiaId, authentication);
    }

    /**
     * Verifica si el usuario puede crear una membresía para otro usuario
     */
    public boolean canCreateFor(Integer targetUserId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Los ADMIN pueden crear para cualquiera
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            return true;
        }

        // Los usuarios solo pueden crear para sí mismos
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuario.getId().equals(targetUserId);
    }
}