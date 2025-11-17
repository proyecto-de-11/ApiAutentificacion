package org.esfe.configuracion.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración principal de seguridad de Spring Security
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Configurar CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Deshabilitar CSRF (no lo necesitamos con JWT)
                .csrf(csrf -> csrf.disable())
                // Configurar autorización de requests
                .authorizeHttpRequests(authRequest -> authRequest
                        // ========== RUTAS PÚBLICAS ==========
                        .requestMatchers(
                                // Endpoint raíz
                                "/",

                                // Swagger/OpenAPI
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",

                                // Autenticación (LOGIN/REGISTRO/LOGOUT)
                                "/api/auth/**",

                                // Health checks (para monitoreo)
                                "/actuator/**",
                                "/health",

                                // Manejo de errores
                                "/error"
                        ).permitAll()

                        // ========== RUTAS PROTEGIDAS POR ROL ==========
                        // Usuarios - solo ADMIN puede hacer todo
                        .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR")

                        // Roles - solo ADMIN
                        .requestMatchers("/api/roles/**").hasRole("ADMINISTRADOR")

                        // Documentos Legales - ADMIN puede crear/editar, otros solo ver
                        .requestMatchers("/api/documentoslegales/lista", "/api/documentoslegales/{id}").authenticated()
                        .requestMatchers("/api/documentoslegales/**").hasRole("ADMINISTRADOR")

                        // Membresías - ADMIN puede gestionar, usuarios pueden ver
                        .requestMatchers("/api/membresias/lista", "/api/membresias/{id}").authenticated()
                        .requestMatchers("/api/membresias/**").hasRole("ADMINISTRADOR")

                        // Tipos de Deporte - todos pueden ver, solo ADMIN modifica
                        .requestMatchers("/api/tiposdeporte/lista", "/api/tiposdeporte/{id}").authenticated()
                        .requestMatchers("/api/tiposdeporte/**").hasRole("ADMINISTRADOR")

                        // Perfiles - cada usuario puede ver/editar su propio perfil
                        .requestMatchers("/api/perfiles/**").authenticated()

                        // Preferencias - cada usuario gestiona las suyas
                        .requestMatchers("/api/preferencias/**").authenticated()

                        // Aceptaciones de términos
                        .requestMatchers("/api/aceptaciones/**").authenticated()

                        // Usuario-Membresías
                        .requestMatchers("/api/usuario-membresias/**").authenticated()

                        // ========== TODAS LAS DEMÁS RUTAS REQUIEREN AUTENTICACIÓN ==========
                        .anyRequest().authenticated()
                )
                // Configurar session management (STATELESS para JWT)
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configurar authentication provider
                .authenticationProvider(authProvider)
                // Añadir el filtro JWT antes del filtro de autenticación de Spring
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Configuración de CORS
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Orígenes permitidos
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8080",     // Gateway local
                "https://*.onrender.com"    // Servicios en Render
        ));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Permitir credenciales
        configuration.setAllowCredentials(true);

        // Headers expuestos
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}