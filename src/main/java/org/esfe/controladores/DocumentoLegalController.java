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

    @GetMapping
    public ResponseEntity<Page<DocumentoLegalSalidaDto>> mostrarTodosPaginadosYFiltrados(
            @RequestParam(required = false) Optional<String> busqueda,
            Pageable pageable) {

        Page<DocumentoLegalSalidaDto> documentosPage = 
            documentoLegalService.obtenerDocumentosPaginadosYFiltrados(busqueda, pageable);

        return ResponseEntity.ok(documentosPage);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<DocumentoLegalSalidaDto>> mostrarTodos() {
        List<DocumentoLegalSalidaDto> documentos = documentoLegalService.obtenerTodos();
        if (!documentos.isEmpty()) {
            return ResponseEntity.ok(documentos);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoLegalSalidaDto> buscarPorId(@PathVariable Integer id) {
        return documentoLegalService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody DocumentoLegalGuardarDto dto) {
        try {
            DocumentoLegalSalidaDto nuevoDocumento = documentoLegalService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDocumento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el Documento Legal: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id, @Valid @RequestBody DocumentoLegalModificarDto dto) {
        try {
            dto.setId(id); 
            DocumentoLegalSalidaDto actualizado = documentoLegalService.editar(dto);
            return ResponseEntity.ok(actualizado);
        } catch (NoSuchElementException e) {
            // Retorna 404 NOT FOUND si el ID no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
             // Retorna 409 CONFLICT si hay una violación de unicidad
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // Otros errores, como problemas de base de datos
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el Documento Legal: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            documentoLegalService.eliminarPorId(id);
            return ResponseEntity.ok("Documento Legal eliminado correctamente.");
        } catch (NoSuchElementException e) {
            // Retorna 404 NOT FOUND si el ID no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el Documento Legal: " + e.getMessage());
        }
    }
}