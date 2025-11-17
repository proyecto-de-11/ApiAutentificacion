package org.esfe.controladores;

import org.esfe.dtos.seguridad.UsuarioLogin;
import org.esfe.dtos.seguridad.UsuarioRegistrar;
import org.esfe.dtos.seguridad.UsuarioToken;
import org.esfe.dtos.usuario.UsuarioGuardarDto;
import org.esfe.dtos.usuario.UsuarioSalidaDto;
import org.esfe.modelos.Usuario;
import org.esfe.repositorios.IUsuarioRepository;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.esfe.servicios.seguridad.JwtService;
import org.esfe.servicios.seguridad.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador de Autenticación
 * Maneja login, logout, registro y validación de tokens
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    /**
     * LOGIN - Autenticar usuario y generar token JWT
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UsuarioLogin loginDto) {
        try {
            // Autenticar con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Obtener el usuario autenticado
            Usuario usuario = (Usuario) authentication.getPrincipal();

            // Generar token JWT
            String token = jwtService.getToken(usuario);

            // Crear respuesta con información adicional
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", usuario.getId());
            response.put("email", usuario.getEmail());
            response.put("rol", usuario.getRol() != null ? usuario.getRol().getNombre() : null);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Credenciales inválidas");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * REGISTRAR - Crear nuevo usuario
     * POST /api/auth/registrar
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody UsuarioGuardarDto usuarioDto) {
        try {
            // Verificar si el email ya existe
            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioDto.getEmail());
            if (usuarioExistente.isPresent()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "El email ya está registrado");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }

            // Crear el usuario
            UsuarioSalidaDto usuarioCreado = usuarioService.crear(usuarioDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al registrar usuario");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * LOGOUT - Invalidar token JWT
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Token no encontrado"));
        }

        String token = authHeader.substring(7);

        try {
            // Verificar que el token sea válido antes de invalidarlo
            String email = jwtService.getUsernameFromToken(token);

            if (email != null) {
                // Añadir el token a la lista negra
                tokenBlacklistService.blacklistToken(token);

                // Limpiar el contexto de seguridad
                SecurityContextHolder.clearContext();

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Sesión cerrada exitosamente");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Token inválido"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Error al cerrar sesión: " + e.getMessage()));
        }
    }

    /**
     * VALIDATE - Validar token JWT
     * GET /api/auth/validate
     */
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", "Token no encontrado"));
        }

        String token = authHeader.substring(7);

        try {
            // Verificar si el token está en la lista negra
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "message", "Token ha sido invalidado"));
            }

            String email = jwtService.getUsernameFromToken(token);
            Long userId = jwtService.getUserIdFromToken(token);

            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "message", "Usuario no encontrado"));
            }

            Usuario usuario = usuarioOpt.get();
            boolean isValid = jwtService.isTokenValid(token, usuario);

            if (isValid) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("userId", userId);
                response.put("email", email);
                response.put("rol", usuario.getRol() != null ? usuario.getRol().getNombre() : null);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "message", "Token inválido o expirado"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", "Error validando token: " + e.getMessage()));
        }
    }

    /**
     * ENDPOINT DE DEBUG - Verificar password (REMOVER EN PRODUCCIÓN)
     * POST /api/auth/debug-password
     */
    @PostMapping("/debug-password")
    public ResponseEntity<String> debugPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String passwordRaw = request.get("password");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        boolean matches = passwordEncoder.matches(passwordRaw, usuario.getPassword());

        return ResponseEntity.ok(String.format(
                "Usuario: %s\nPassword en DB: %s\nPassword raw: %s\nCoinciden: %s\nUsuario habilitado: %s\nRol: %s",
                usuario.getEmail(),
                usuario.getPassword().substring(0, 20) + "...",
                passwordRaw,
                matches,
                usuario.isEnabled(),
                usuario.getRol() != null ? usuario.getRol().getNombre() : "Sin rol"
        ));
    }

    /**
     * ENDPOINT DE DEBUG - Regenerar password (REMOVER EN PRODUCCIÓN)
     * POST /api/auth/debug-regenerar
     */
    @PostMapping("/debug-regenerar")
    public ResponseEntity<String> regenerarPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String nuevaPassword = request.get("password");

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }

            Usuario usuario = usuarioOpt.get();
            String passwordHasheada = passwordEncoder.encode(nuevaPassword);

            usuario.setContrasena(passwordHasheada);
            usuarioRepository.save(usuario);

            boolean matches = passwordEncoder.matches(nuevaPassword, passwordHasheada);

            return ResponseEntity.ok(String.format(
                    "Password actualizada correctamente\nVerificación: %s",
                    matches ? "✅ EXITOSA" : "❌ FALLÓ"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}