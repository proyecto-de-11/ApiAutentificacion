package org.esfe.controladores;

import org.esfe.dtos.usuarioMembresias.PreferenciaUsuarioGuardarDTO;
import org.esfe.dtos.usuarioMembresias.PreferenciaUsuarioModificarDTO;
import org.esfe.dtos.usuarioMembresias.PreferenciaUsuarioSalidaDTO;
import org.esfe.servicios.interfaces.IPreferenciaUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/preferencias")
@Validated
public class PreferenciaUsuarioController {

    private final IPreferenciaUsuarioService service;

    public PreferenciaUsuarioController(IPreferenciaUsuarioService service) {
        this.service = service;
    }

    // ✅ Crear: Usuario solo puede crear sus propias preferencias
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<PreferenciaUsuarioSalidaDTO> guardar(@Valid @RequestBody PreferenciaUsuarioGuardarDTO dto,
                                                               Authentication authentication) {
        // Verificar que el usuario solo pueda crear sus propias preferencias
        org.esfe.modelos.Usuario usuario = (org.esfe.modelos.Usuario) authentication.getPrincipal();
        if (!dto.getUsuarioId().equals(usuario.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        PreferenciaUsuarioSalidaDTO salida = service.guardar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(salida.getId()).toUri();
        return ResponseEntity.created(location).body(salida);
    }

    // ✅ Modificar: ADMIN o el mismo usuario
    @PreAuthorize("hasRole('ADMINISTRADOR') or @preferenciasSecurity.isOwner(#id, authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<PreferenciaUsuarioSalidaDTO> modificar(@PathVariable Integer id,
                                                                 @Valid @RequestBody PreferenciaUsuarioModificarDTO dto) {
        dto.setId(id);
        PreferenciaUsuarioSalidaDTO salida = service.modificar(dto);
        return ResponseEntity.ok(salida);
    }

    // ✅ Obtener por ID: ADMIN o el mismo usuario
    @PreAuthorize("hasRole('ADMINISTRADOR') or @preferenciasSecurity.isOwner(#id, authentication)")
    @GetMapping("/{id}")
    public ResponseEntity<PreferenciaUsuarioSalidaDTO> obtenerPorId(@PathVariable Integer id) {
        PreferenciaUsuarioSalidaDTO salida = service.obtenerPorId(id);
        return ResponseEntity.ok(salida);
    }

    // ✅ Obtener por usuario: ADMIN o el mismo usuario
    //or #usuarioId == authentication.principal.id
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<PreferenciaUsuarioSalidaDTO> obtenerPorUsuario(@PathVariable Integer usuarioId) {
        PreferenciaUsuarioSalidaDTO salida = service.obtenerPorUsuarioId(usuarioId);
        return ResponseEntity.ok(salida);
    }

    // ✅ Listar todos: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<PreferenciaUsuarioSalidaDTO>> obtenerTodos() {
        List<PreferenciaUsuarioSalidaDTO> lista = service.obtenerTodos();
        return ResponseEntity.ok(lista);
    }

    // ✅ Eliminar: ADMIN
    // or @preferenciasSecurity.isOwner(#id, authentication)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Manejo de excepciones
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}