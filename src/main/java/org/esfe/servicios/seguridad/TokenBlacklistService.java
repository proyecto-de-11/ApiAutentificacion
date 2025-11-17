package org.esfe.servicios.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio para gestionar tokens invalidados (blacklist)
 * Incluye limpieza automática de tokens expirados
 */
@Service
@EnableScheduling
public class TokenBlacklistService {

    // Almacena tokens con su fecha de expiración
    private final Map<String, LocalDateTime> blacklistedTokens = new ConcurrentHashMap<>();

    // Para compatibilidad con versión anterior
    private final Set<String> blacklistedTokensSet = ConcurrentHashMap.newKeySet();

    @Autowired
    private JwtService jwtService;

    /**
     * Añade un token a la lista negra (invalidación)
     * @param token Token a invalidar
     */
    public void blacklistToken(String token) {
        try {
            // Obtener fecha de expiración del token
            LocalDateTime expirationDate = getTokenExpirationDate(token);
            blacklistedTokens.put(token, expirationDate);
            blacklistedTokensSet.add(token);
        } catch (Exception e) {
            // Si hay error obteniendo la expiración, añadir con expiración de 24 horas
            blacklistedTokens.put(token, LocalDateTime.now().plusHours(24));
            blacklistedTokensSet.add(token);
        }
    }

    /**
     * Añade un token a la lista negra con tiempo de expiración específico
     * @param token Token a invalidar
     * @ param expirationTime Tiempo de expiración en horas
     */
    public void blacklistToken(String token, int expirationHours) {
        LocalDateTime expirationDate = LocalDateTime.now().plusHours(expirationHours);
        blacklistedTokens.put(token, expirationDate);
        blacklistedTokensSet.add(token);
    }

    /**
     * Verifica si un token está en la lista negra
     * @param token Token a verificar
     * @return true si está en la lista negra, false en caso contrario
     */
    public boolean isTokenBlacklisted(String token) {
        // Verificar en ambas estructuras para compatibilidad
        return blacklistedTokens.containsKey(token) || blacklistedTokensSet.contains(token);
    }

    /**
     * Remueve un token específico de la lista negra
     * @param token Token a remover
     * @return true si el token fue removido, false si no existía
     */
    public boolean removeToken(String token) {
        boolean removedFromMap = blacklistedTokens.remove(token) != null;
        boolean removedFromSet = blacklistedTokensSet.remove(token);
        return removedFromMap || removedFromSet;
    }

    /**
     * Limpia tokens expirados de la lista negra
     * Este método se ejecuta automáticamente cada hora
     */
    @Scheduled(fixedRate = 3600000) // Ejecutar cada 1 hora (3600000 ms)
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        int removedCount = 0;

        // Iterar sobre los tokens y remover los expirados
        for (Map.Entry<String, LocalDateTime> entry : blacklistedTokens.entrySet()) {
            if (entry.getValue().isBefore(now)) {
                String token = entry.getKey();
                blacklistedTokens.remove(token);
                blacklistedTokensSet.remove(token);
                removedCount++;
            }
        }

        if (removedCount > 0) {
            System.out.println("🧹 TokenBlacklistService: " + removedCount +
                    " tokens expirados removidos de la blacklist");
        }
    }

    /**
     * Limpieza manual de tokens expirados
     * @return Cantidad de tokens removidos
     */
    public int manualCleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        int removedCount = 0;

        for (Map.Entry<String, LocalDateTime> entry : blacklistedTokens.entrySet()) {
            if (entry.getValue().isBefore(now)) {
                String token = entry.getKey();
                blacklistedTokens.remove(token);
                blacklistedTokensSet.remove(token);
                removedCount++;
            }
        }

        return removedCount;
    }

    /**
     * Obtiene el tamaño actual de la lista negra (para debugging/monitoring)
     * @return número de tokens en la lista negra
     */
    public int getBlacklistSize() {
        return blacklistedTokens.size();
    }

    /**
     * Obtiene estadísticas de la blacklist
     * @return Map con estadísticas
     */
    public Map<String, Object> getBlacklistStats() {
        LocalDateTime now = LocalDateTime.now();
        long expiredCount = blacklistedTokens.values().stream()
                .filter(expiration -> expiration.isBefore(now))
                .count();

        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("totalTokens", blacklistedTokens.size());
        stats.put("expiredTokens", expiredCount);
        stats.put("activeTokens", blacklistedTokens.size() - expiredCount);
        stats.put("lastCleanup", LocalDateTime.now());

        return stats;
    }

    /**
     * Verifica si un token específico ha expirado en la blacklist
     * @param token Token a verificar
     * @return true si ha expirado, false si aún está activo o no existe
     */
    public boolean isTokenExpiredInBlacklist(String token) {
        LocalDateTime expirationDate = blacklistedTokens.get(token);
        if (expirationDate == null) {
            return false;
        }
        return expirationDate.isBefore(LocalDateTime.now());
    }

    /**
     * Obtiene el tiempo restante hasta que un token expire en la blacklist
     * @param token Token a verificar
     * @return Horas restantes hasta expiración, -1 si no está en la blacklist
     */
    public long getTimeUntilExpiration(String token) {
        LocalDateTime expirationDate = blacklistedTokens.get(token);
        if (expirationDate == null) {
            return -1;
        }

        LocalDateTime now = LocalDateTime.now();
        if (expirationDate.isBefore(now)) {
            return 0;
        }

        return java.time.Duration.between(now, expirationDate).toHours();
    }

    /**
     * Limpia completamente la lista negra (solo para testing)
     */
    public void clearBlacklist() {
        blacklistedTokens.clear();
        blacklistedTokensSet.clear();
    }

    /**
     * Invalida todos los tokens de un usuario específico
     * @param userId ID del usuario
     */
    public void blacklistAllUserTokens(Long userId) {
        // Este método requeriría almacenar los tokens por usuario
        // Por ahora es un placeholder para futura implementación
        System.out.println("⚠️ Blacklisting all tokens for user: " + userId);
    }

    /**
     * Obtiene la fecha de expiración de un token
     * @param token Token JWT
     * @return Fecha de expiración
     */
    private LocalDateTime getTokenExpirationDate(String token) {
        try {
            java.util.Date expirationDate = jwtService.getClaim(token, claims -> claims.getExpiration());
            return expirationDate.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (Exception e) {
            // Si hay error, retornar fecha por defecto (24 horas)
            return LocalDateTime.now().plusHours(24);
        }
    }

    /**
     * Método de limpieza programada más agresiva (cada 6 horas)
     */
    @Scheduled(fixedRate = 21600000) // Ejecutar cada 6 horas
    public void aggressiveCleanup() {
        int removed = manualCleanExpiredTokens();
        if (removed > 0) {
            System.out.println("🧹 Limpieza agresiva: " + removed + " tokens removidos");
        }
    }

    /**
     * Método de reporte diario de estadísticas
     */
    @Scheduled(cron = "0 0 0 * * ?") // Ejecutar a medianoche todos los días
    public void dailyReport() {
        Map<String, Object> stats = getBlacklistStats();
        System.out.println("📊 Reporte diario de Blacklist:");
        System.out.println("   - Total tokens: " + stats.get("totalTokens"));
        System.out.println("   - Tokens expirados: " + stats.get("expiredTokens"));
        System.out.println("   - Tokens activos: " + stats.get("activeTokens"));
    }
}