package org.esfe.controladores;

import org.esfe.dtos.usuario.PerfilSalidaDto;
import org.esfe.dtos.usuario.PerfilGuardarDto;
import org.esfe.dtos.usuario.PerfilModificarDto;
import org.esfe.servicios.interfaces.IPerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/perfiles")
@Validated
public class PerfilController {

    @Autowired
    private IPerfilService perfilService;

    @GetMapping
    public List<PerfilSalidaDto> listarTodos() {
        return perfilService.listarTodos();
    }

    @GetMapping("/{id}")
    public PerfilSalidaDto obtenerPorId(@PathVariable Integer id) {
        return perfilService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<PerfilSalidaDto> crear(@Valid @RequestBody PerfilGuardarDto dto) {
        PerfilSalidaDto created = perfilService.crear(dto);
        return ResponseEntity.created(URI.create("/perfiles/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public PerfilSalidaDto actualizar(@PathVariable Integer id, @Valid @RequestBody PerfilModificarDto dto) {
        return perfilService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        perfilService.eliminar(id);
    }

    // Nuevo: obtener perfil por usuarioId
    @GetMapping("/usuario/{usuarioId}")
    public PerfilSalidaDto obtenerPorUsuarioId(@PathVariable Integer usuarioId) {
        return perfilService.obtenerPorUsuarioId(usuarioId);
    }

    // Nuevo: buscar por ciudad (case-insensitive)
    @GetMapping("/ciudad/{ciudad}")
    public List<PerfilSalidaDto> buscarPorCiudad(@PathVariable String ciudad) {
        return perfilService.buscarPorCiudad(ciudad);
    }

    // Nuevo: buscar por país y ciudad
    @GetMapping("/buscar")
    public List<PerfilSalidaDto> buscarPorPaisYCiudad(@RequestParam String pais, @RequestParam String ciudad) {
        return perfilService.buscarPorPaisYCiudad(pais, ciudad);
    }
}
