package org.esfe.configuracion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, String> body = new HashMap<>();
        // Usamos la razón si está, sino el mensaje de la excepción
        String message = ex.getReason() != null ? ex.getReason() : ex.getMessage();
        body.put("message", message);
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    /**
     * Maneja excepciones de acceso denegado (Spring Security)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Acceso denegado");
        body.put("message", "No tienes permisos para acceder a este recurso");
        body.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    /**
     * Maneja excepciones de seguridad personalizadas
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, String>> handleSecurityException(SecurityException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Violación de seguridad");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "No tienes permisos para realizar esta acción");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    /**
     * Maneja excepciones genéricas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> body = new HashMap<>();
        body.put("message", "Ocurrió un error interno");
        body.put("details", ex.getMessage());

        // Log para debugging (en producción usar un logger apropiado)
        System.err.println("Error no manejado: " + ex.getClass().getName());
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}