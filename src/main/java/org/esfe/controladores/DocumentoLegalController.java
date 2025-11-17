package org.esfe.controladores;

import org.esfe.dtos.documentosLegales.DocumentoLegalGuardarDto;
import org.esfe.dtos.documentosLegales.DocumentoLegalModificarDto;
import org.esfe.dtos.documentosLegales.DocumentoLegalSalidaDto;
import org.esfe.servicios.interfaces.IDocumentoLegalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/documentoslegales")
public class DocumentoLegalController {

    @Autowired
    private IDocumentoLegalService documentoLegalService;

    // ✅ Ver lista paginada: Todos los usuarios autenticados
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<DocumentoLegalSalidaDto>> mostrarTodosPaginadosYFiltrados(
            @RequestParam(required = false) Optional<String> busqueda,
            Pageable pageable) {

        Page<DocumentoLegalSalidaDto> documentosPage =
                documentoLegalService.obtenerDocumentosPaginadosYFiltrados(busqueda, pageable);

        return ResponseEntity.ok(documentosPage);
    }

    // ✅ Ver lista completa: Todos los usuarios autenticados
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/lista")
    public ResponseEntity<List<DocumentoLegalSalidaDto>> mostrarTodos() {
        List<DocumentoLegalSalidaDto> documentos = documentoLegalService.obtenerTodos();
        if (!documentos.isEmpty()) {
            return ResponseEntity.ok(documentos);
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ Ver por ID: Todos los usuarios autenticados
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoLegalSalidaDto> buscarPorId(@PathVariable Integer id) {
        return documentoLegalService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Crear: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody DocumentoLegalGuardarDto dto) {
        try {
            DocumentoLegalSalidaDto nuevoDocumento = documentoLegalService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDocumento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el Documento Legal: " + e.getMessage());
        }
    }

    // ✅ Editar: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id, @Valid @RequestBody DocumentoLegalModificarDto dto) {
        try {
            dto.setId(id);
            DocumentoLegalSalidaDto actualizado = documentoLegalService.editar(dto);
            return ResponseEntity.ok(actualizado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el Documento Legal: " + e.getMessage());
        }
    }

    // ✅ Eliminar: Solo ADMIN
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            documentoLegalService.eliminarPorId(id);
            return ResponseEntity.ok("Documento Legal eliminado correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el Documento Legal: " + e.getMessage());
        }
    }
}