package pe.edu.upc.oss.group1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.mapper.DispositivoMapper;
import pe.edu.upc.oss.group1.dto.request.DispositivoRequest;
import pe.edu.upc.oss.group1.dto.request.DispositivoEstadoRequest;
import pe.edu.upc.oss.group1.dto.response.DispositivoResponse;
import pe.edu.upc.oss.group1.entity.Dispositivo;
import pe.edu.upc.oss.group1.service.DispositivoService;

import java.util.List;

/**
 * Controller REST para gestión de dispositivos IT.
 */
@RestController
@RequestMapping("/api/v1/dispositivos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dispositivos", description = "Gestión de activos IT y dispositivos")
public class DispositivoController {

    private final DispositivoService dispositivoService;

    @GetMapping
    @Operation(summary = "Listar todos los dispositivos")
    public ResponseEntity<List<DispositivoResponse>> findAll() {
        log.info("GET /api/v1/dispositivos - Obteniendo todos los dispositivos");
        List<Dispositivo> dispositivos = dispositivoService.findAll();
        List<DispositivoResponse> response = DispositivoMapper.toResponseList(dispositivos);
        log.info("Se encontraron {} dispositivos", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Listar dispositivos disponibles para asignación")
    public ResponseEntity<List<DispositivoResponse>> findDisponibles() {
        log.info("GET /api/v1/dispositivos/disponibles - Obteniendo dispositivos disponibles");
        List<Dispositivo> dispositivos = dispositivoService.findDisponibles();
        List<DispositivoResponse> response = DispositivoMapper.toResponseList(dispositivos);
        log.info("Se encontraron {} dispositivos disponibles", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/disponibles/page")
    @Operation(summary = "Listar dispositivos disponibles con paginación")
    public ResponseEntity<Page<DispositivoResponse>> findDisponiblesPaginated(Pageable pageable) {
        log.info("GET /api/v1/dispositivos/disponibles/page - Obteniendo dispositivos disponibles con paginación");
        Page<Dispositivo> dispositivos = dispositivoService.findDisponibles(pageable);
        Page<DispositivoResponse> response = dispositivos.map(DispositivoMapper::toResponse);
        log.info("Se encontraron {} dispositivos disponibles en página {}", response.getContent().size(), pageable.getPageNumber());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener dispositivo por ID")
    public ResponseEntity<DispositivoResponse> findById(@PathVariable Integer id) {
        log.info("GET /api/v1/dispositivos/{} - Obteniendo dispositivo por ID", id);
        Dispositivo dispositivo = dispositivoService.findByIdWithRelations(id);
        DispositivoResponse response = DispositivoMapper.toResponse(dispositivo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Obtener dispositivo por código de activo")
    public ResponseEntity<DispositivoResponse> findByCodigoActivo(@PathVariable String codigo) {
        log.info("GET /api/v1/dispositivos/codigo/{} - Obteniendo dispositivo por código", codigo);
        Dispositivo dispositivo = dispositivoService.findByCodigoActivo(codigo);
        DispositivoResponse response = DispositivoMapper.toResponse(dispositivo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tipo/{tipoId}")
    @Operation(summary = "Listar dispositivos por tipo")
    public ResponseEntity<List<DispositivoResponse>> findByTipo(@PathVariable Integer tipoId) {
        log.info("GET /api/v1/dispositivos/tipo/{} - Obteniendo dispositivos por tipo", tipoId);
        List<Dispositivo> dispositivos = dispositivoService.findByTipo(tipoId);
        List<DispositivoResponse> response = DispositivoMapper.toResponseList(dispositivos);
        log.info("Se encontraron {} dispositivos del tipo {}", response.size(), tipoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo dispositivo")
    public ResponseEntity<DispositivoResponse> create(@Valid @RequestBody DispositivoRequest request) {
        log.info("POST /api/v1/dispositivos - Creando dispositivo: {}", request.getCodigoActivo());
        Dispositivo dispositivo = DispositivoMapper.toEntity(request);
        Dispositivo created = dispositivoService.create(dispositivo);
        DispositivoResponse response = DispositivoMapper.toResponse(created);
        log.info("Dispositivo creado exitosamente con ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar dispositivo")
    public ResponseEntity<DispositivoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody DispositivoRequest request) {
        log.info("PUT /api/v1/dispositivos/{} - Actualizando dispositivo", id);
        Dispositivo dispositivo = DispositivoMapper.toEntity(request);
        Dispositivo updated = dispositivoService.update(id, dispositivo);
        DispositivoResponse response = DispositivoMapper.toResponse(updated);
        log.info("Dispositivo actualizado exitosamente con ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de dispositivo")
    public ResponseEntity<DispositivoResponse> updateEstado(
            @PathVariable Integer id,
            @Valid @RequestBody DispositivoEstadoRequest request) {
        log.info("PATCH /api/v1/dispositivos/{}/estado - Actualizando estado", id);
        Dispositivo updated = dispositivoService.updateEstado(id, request.getEstadoId(), request.getObservacion());
        DispositivoResponse response = DispositivoMapper.toResponse(updated);
        log.info("Estado de dispositivo actualizado exitosamente. ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar dispositivo")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("DELETE /api/v1/dispositivos/{} - Eliminando dispositivo", id);
        dispositivoService.delete(id);
        log.info("Dispositivo eliminado exitosamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
