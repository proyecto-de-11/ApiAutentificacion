package org.esfe.servicios.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.esfe.modelos.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${security.jwt.expiration-time:86400000}")
    private long EXPIRATION_TIME;

    /**
     * Genera un token JWT para el usuario
     */
    public String getToken(Usuario usuario) {
        List<String> roles = new ArrayList<>();
        if (usuario.getRol() != null) {
            roles.add("ROLE_" + usuario.getRol().getNombre().toUpperCase());
        }

        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", roles);
        extraClaims.put("userId", usuario.getId().longValue());
        extraClaims.put("email", usuario.getEmail());

        return generarToken(extraClaims, usuario);
    }

    /**
     * Genera el token JWT con los claims y el usuario
     */
    private String generarToken(HashMap<String, Object> extraClaims, UserDetails usuario) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Obtiene la clave de firma
     */
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrae el username (email) del token
     */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * Extrae el userId del token
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims.get("userId", Long.class);
    }

    /**
     * Valida si el token es válido para el usuario
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene todos los claims del token
     */
    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene un claim específico del token
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Obtiene la fecha de expiración del token
     */
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * Verifica si el token ha expirado
     */
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}