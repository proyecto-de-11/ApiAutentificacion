package org.esfe.configuracion.seguridad;

import org.esfe.modelos.Usuario;
import org.esfe.modelos.UsuarioAceptacionTermino;
import org.esfe.repositorios.IUsuarioAceptacionTerminoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Helper para validar permisos de acceso a aceptaciones de términos
 * Lanza excepciones apropiadas en lugar de retornar false
 */
@Component("aceptacionSecurity")
public class AceptacionSecurityHelper {

    @Autowired
    private IUsuarioAceptacionTerminoRepository aceptacionRepository;

    /**
     * Verifica si el usuario autenticado es el propietario de la aceptación
     * @param aceptacionId ID de la aceptación
     * @param authentication Objeto de autenticación
     * @return true si es el propietario
     * @throws AccessDeniedException si no es el propietario o no está autenticado
     */
    public boolean isOwner(Integer aceptacionId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Debes iniciar sesión para acceder a este recurso");
        }

        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            Optional<UsuarioAceptacionTermino> aceptacionOpt = aceptacionRepository.findById(aceptacionId);

            if (aceptacionOpt.isEmpty()) {
                throw new AccessDeniedException("La aceptación de términos solicitada no existe");
            }

            UsuarioAceptacionTermino aceptacion = aceptacionOpt.get();

            if (aceptacion.getUsuario() == null || !aceptacion.getUsuario().getId().equals(usuario.getId())) {
                throw new AccessDeniedException("No tienes permiso para acceder a esta aceptación de términos");
            }

            return true;
        } catch (ClassCastException e) {
            throw new AccessDeniedException("Error de autenticación: token inválido");
        }
    }

    /**
     * Verifica si el usuario puede acceder a la aceptación (es suya o es ADMIN)
     * @param aceptacionId ID de la aceptación
     * @param authentication Objeto de autenticación
     * @return true si puede acceder
     * @throws AccessDeniedException si no tiene permisos
     */
    public boolean canAccess(Integer aceptacionId, Authentication authentication) {
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
        return isOwner(aceptacionId, authentication);
    }
}