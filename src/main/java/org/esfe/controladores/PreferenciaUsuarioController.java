package org.esfe.controladores;

import org.esfe.dtos.usuarioMembresias.PreferenciaUsuarioGuardarDTO;
import org.esfe.dtos.usuarioMembresias.PreferenciaUsuarioModificarDTO;
import org.esfe.dtos.usuarioMembresias.PreferenciaUsuarioSalidaDTO;
import org.esfe.servicios.interfaces.IPreferenciaUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<PreferenciaUsuarioSalidaDTO> guardar(@Valid @RequestBody PreferenciaUsuarioGuardarDTO dto) {
        PreferenciaUsuarioSalidaDTO salida = service.guardar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(salida.getId()).toUri();
        return ResponseEntity.created(location).body(salida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreferenciaUsuarioSalidaDTO> modificar(@PathVariable Integer id, @Valid @RequestBody PreferenciaUsuarioModificarDTO dto) {
        // Aseguramos que el id venga por la ruta y lo asignamos al DTO antes de modificar
        dto.setId(id);
        PreferenciaUsuarioSalidaDTO salida = service.modificar(dto);
        return ResponseEntity.ok(salida);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreferenciaUsuarioSalidaDTO> obtenerPorId(@PathVariable Integer id) {
        PreferenciaUsuarioSalidaDTO salida = service.obtenerPorId(id);
        return ResponseEntity.ok(salida);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<PreferenciaUsuarioSalidaDTO> obtenerPorUsuario(@PathVariable Integer usuarioId) {
        PreferenciaUsuarioSalidaDTO salida = service.obtenerPorUsuarioId(usuarioId);
        return ResponseEntity.ok(salida);
    }

    @GetMapping
    public ResponseEntity<List<PreferenciaUsuarioSalidaDTO>> obtenerTodos() {
        List<PreferenciaUsuarioSalidaDTO> lista = service.obtenerTodos();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Manejo de excepciones local al controller:
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
