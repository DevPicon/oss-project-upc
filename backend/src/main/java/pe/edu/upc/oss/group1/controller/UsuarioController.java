package pe.edu.upc.oss.group1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.mapper.UsuarioMapper;
import pe.edu.upc.oss.group1.dto.request.UsuarioRequest;
import pe.edu.upc.oss.group1.dto.response.UsuarioResponse;
import pe.edu.upc.oss.group1.entity.Usuario;
import pe.edu.upc.oss.group1.service.UsuarioService;

import java.util.List;

/**
 * Controller REST para gestión de usuarios del sistema.
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<UsuarioResponse>> findAll() {
        log.info("GET /api/v1/usuarios - Obteniendo todos los usuarios");
        List<Usuario> usuarios = usuarioService.findAll();
        List<UsuarioResponse> response = UsuarioMapper.toResponseList(usuarios);
        log.info("Se encontraron {} usuarios", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar usuarios activos")
    public ResponseEntity<List<UsuarioResponse>> findAllActive() {
        log.info("GET /api/v1/usuarios/activos - Obteniendo usuarios activos");
        List<Usuario> usuarios = usuarioService.findAllActivos();
        List<UsuarioResponse> response = UsuarioMapper.toResponseList(usuarios);
        log.info("Se encontraron {} usuarios activos", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable Integer id) {
        log.info("GET /api/v1/usuarios/{} - Obteniendo usuario por ID", id);
        Usuario usuario = usuarioService.findById(id);
        UsuarioResponse response = UsuarioMapper.toResponse(usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Obtener usuario por nombre de usuario")
    public ResponseEntity<UsuarioResponse> findByUsername(@PathVariable String username) {
        log.info("GET /api/v1/usuarios/username/{} - Obteniendo usuario por username", username);
        Usuario usuario = usuarioService.findByUsername(username);
        UsuarioResponse response = UsuarioMapper.toResponse(usuario);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario")
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioRequest request) {
        log.info("POST /api/v1/usuarios - Creando usuario: {}", request.getUsername());
        Usuario usuario = UsuarioMapper.toEntity(request);
        Usuario created = usuarioService.create(usuario);
        UsuarioResponse response = UsuarioMapper.toResponse(created);
        log.info("Usuario creado exitosamente con ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<UsuarioResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequest request) {
        log.info("PUT /api/v1/usuarios/{} - Actualizando usuario", id);
        Usuario usuario = UsuarioMapper.toEntity(request);
        Usuario updated = usuarioService.update(id, usuario);
        UsuarioResponse response = UsuarioMapper.toResponse(updated);
        log.info("Usuario actualizado exitosamente con ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar usuario")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("DELETE /api/v1/usuarios/{} - Desactivando usuario", id);
        usuarioService.delete(id);
        log.info("Usuario desactivado exitosamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
