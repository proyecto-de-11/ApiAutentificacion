package org.esfe.servicios.seguridad;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio para gestionar tokens invalidados (blacklist)
 * Usamos ConcurrentHashMap para thread-safety
 */
@Service
public class TokenBlacklistService {

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    /**
     * Añade un token a la lista negra (invalidación)
     * @param token Token a invalidar
     */
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    /**
     * Verifica si un token está en la lista negra
     * @param token Token a verificar
     * @return true si está en la lista negra, false en caso contrario
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    /**
     * Limpia tokens expirados de la lista negra
     * Este método debería ejecutarse periódicamente con un scheduled task
     */
    public void cleanExpiredTokens() {
        // TODO: Implementar limpieza de tokens expirados
        // Podría usar un ScheduledExecutorService o @Scheduled
    }

    /**
     * Obtiene el tamaño actual de la lista negra (para debugging/monitoring)
     * @return número de tokens en la lista negra
     */
    public int getBlacklistSize() {
        return blacklistedTokens.size();
    }

    /**
     * Limpia completamente la lista negra (solo para testing)
     */
    public void clearBlacklist() {
        blacklistedTokens.clear();
    }
}