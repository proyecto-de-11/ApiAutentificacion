package org.esfe.configuracion.seguridad;

import org.esfe.modelos.PreferenciaUsuario;
import org.esfe.modelos.Usuario;
import org.esfe.repositorios.IPreferenciaUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Helper para validar permisos de acceso a preferencias de usuario
 * Lanza excepciones apropiadas en lugar de retornar false
 */
@Component("preferenciasSecurity")
public class PreferenciasSecurityHelper {

    @Autowired
    private IPreferenciaUsuarioRepository preferenciaRepository;

    /**
     * Verifica si el usuario autenticado es el propietario de las preferencias
     * @param preferenciaId ID de la preferencia
     * @param authentication Objeto de autenticación
     * @return true si es el propietario
     * @throws AccessDeniedException si no es el propietario o no está autenticado
     */
    public boolean isOwner(Integer preferenciaId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Debes iniciar sesión para acceder a este recurso");
        }

        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            Optional<PreferenciaUsuario> preferenciaOpt = preferenciaRepository.findById(preferenciaId);

            if (preferenciaOpt.isEmpty()) {
                throw new AccessDeniedException("Las preferencias solicitadas no existen");
            }

            PreferenciaUsuario preferencia = preferenciaOpt.get();

            if (preferencia.getUsuario() == null || !preferencia.getUsuario().getId().equals(usuario.getId())) {
                throw new AccessDeniedException("No tienes permiso para acceder a estas preferencias");
            }

            return true;
        } catch (ClassCastException e) {
            throw new AccessDeniedException("Error de autenticación: token inválido");
        }
    }

    /**
     * Verifica si el usuario puede acceder a las preferencias (son suyas o es ADMIN)
     * @param preferenciaId ID de la preferencia
     * @param authentication Objeto de autenticación
     * @return true si puede acceder
     * @throws AccessDeniedException si no tiene permisos
     */
    public boolean canAccess(Integer preferenciaId, Authentication authentication) {
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
        return isOwner(preferenciaId, authentication);
    }
}