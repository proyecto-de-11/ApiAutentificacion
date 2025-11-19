package org.esfe.configuracion.seguridad;

import org.esfe.modelos.Usuario;
import org.esfe.modelos.UsuarioMembresia;
import org.esfe.repositorios.IUsuarioMembresiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Helper para validar permisos de acceso a membresías de usuario
 * Lanza excepciones apropiadas en lugar de retornar false
 */
@Component("usuarioMembresiaSecurity")
public class UsuarioMembresiaSecurityHelper {

    @Autowired
    private IUsuarioMembresiaRepository usuarioMembresiaRepository;

    /**
     * Verifica si el usuario autenticado es el propietario de la membresía
     * @param usuarioMembresiaId ID de la membresía de usuario
     * @param authentication Objeto de autenticación
     * @return true si es el propietario
     * @throws AccessDeniedException si no es el propietario o no está autenticado
     */
    public boolean isOwner(Integer usuarioMembresiaId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Debes iniciar sesión para acceder a este recurso");
        }

        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            Optional<UsuarioMembresia> membresiaOpt = usuarioMembresiaRepository.findById(usuarioMembresiaId);

            if (membresiaOpt.isEmpty()) {
                throw new AccessDeniedException("La membresía solicitada no existe");
            }

            UsuarioMembresia membresia = membresiaOpt.get();

            if (membresia.getUsuario() == null || !membresia.getUsuario().getId().equals(usuario.getId())) {
                throw new AccessDeniedException("No tienes permiso para acceder a esta membresía");
            }

            return true;
        } catch (ClassCastException e) {
            throw new AccessDeniedException("Error de autenticación: token inválido");
        }
    }

    /**
     * Verifica si el usuario puede acceder a la membresía (es suya o es ADMIN)
     * @param usuarioMembresiaId ID de la membresía de usuario
     * @param authentication Objeto de autenticación
     * @return true si puede acceder
     * @throws AccessDeniedException si no tiene permisos
     */
    public boolean canAccess(Integer usuarioMembresiaId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Debes iniciar sesión para acceder a este recurso");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            return true;
        }

        // Verificar si es el propietario (esto lanzará excepción si no lo es)
        return isOwner(usuarioMembresiaId, authentication);
    }

    /**
     * Verifica si el usuario puede crear una membresía para otro usuario
     * @param targetUserId ID del usuario destino
     * @param authentication Objeto de autenticación
     * @return true si puede crear
     * @throws AccessDeniedException si no tiene permisos
     */
    public boolean canCreateFor(Integer targetUserId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Debes iniciar sesión para realizar esta acción");
        }

        // Los ADMIN pueden crear para cualquiera
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            return true;
        }

        // Los usuarios solo pueden crear para sí mismos
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();

            if (!usuario.getId().equals(targetUserId)) {
                throw new AccessDeniedException("No puedes crear membresías para otros usuarios");
            }

            return true;
        } catch (ClassCastException e) {
            throw new AccessDeniedException("Error de autenticación: token inválido");
        }
    }
}