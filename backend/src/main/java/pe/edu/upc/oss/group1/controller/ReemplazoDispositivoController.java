package pe.edu.upc.oss.group1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.mapper.ReemplazoDispositivoMapper;
import pe.edu.upc.oss.group1.dto.request.ReemplazoDispositivoRequest;
import pe.edu.upc.oss.group1.dto.response.ReemplazoDispositivoResponse;
import pe.edu.upc.oss.group1.entity.ReemplazoDispositivo;
import pe.edu.upc.oss.group1.service.ReemplazoDispositivoService;

import java.util.List;

/**
 * Controller REST para gestión de reemplazos de dispositivos.
 */
@RestController
@RequestMapping("/api/v1/reemplazos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reemplazos", description = "Gestión de reemplazos de dispositivos durante asignaciones activas")
public class ReemplazoDispositivoController {

    private final ReemplazoDispositivoService reemplazoService;

    @GetMapping
    @Operation(summary = "Listar todos los reemplazos")
    public ResponseEntity<List<ReemplazoDispositivoResponse>> findAll() {
        log.info("GET /api/v1/reemplazos - Obteniendo todos los reemplazos");
        List<ReemplazoDispositivo> reemplazos = reemplazoService.findAll();
        List<ReemplazoDispositivoResponse> response = ReemplazoDispositivoMapper.toResponseList(reemplazos);
        log.info("Se encontraron {} reemplazos", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Listar reemplazos pendientes")
    public ResponseEntity<List<ReemplazoDispositivoResponse>> findPendientes() {
        log.info("GET /api/v1/reemplazos/pendientes - Obteniendo reemplazos pendientes");
        List<ReemplazoDispositivo> reemplazos = reemplazoService.findPendientes();
        List<ReemplazoDispositivoResponse> response = ReemplazoDispositivoMapper.toResponseList(reemplazos);
        log.info("Se encontraron {} reemplazos pendientes", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reemplazo por ID")
    public ResponseEntity<ReemplazoDispositivoResponse> findById(@PathVariable Integer id) {
        log.info("GET /api/v1/reemplazos/{} - Obteniendo reemplazo por ID", id);
        ReemplazoDispositivo reemplazo = reemplazoService.findByIdWithRelations(id);
        ReemplazoDispositivoResponse response = ReemplazoDispositivoMapper.toResponse(reemplazo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empleado/{empleadoId}")
    @Operation(summary = "Listar reemplazos de un empleado")
    public ResponseEntity<List<ReemplazoDispositivoResponse>> findByEmpleado(@PathVariable Integer empleadoId) {
        log.info("GET /api/v1/reemplazos/empleado/{} - Obteniendo reemplazos del empleado", empleadoId);
        List<ReemplazoDispositivo> reemplazos = reemplazoService.findByEmpleado(empleadoId);
        List<ReemplazoDispositivoResponse> response = ReemplazoDispositivoMapper.toResponseList(reemplazos);
        log.info("Se encontraron {} reemplazos para el empleado {}", response.size(), empleadoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Crear solicitud de reemplazo")
    public ResponseEntity<ReemplazoDispositivoResponse> create(@Valid @RequestBody ReemplazoDispositivoRequest request) {
        log.info("POST /api/v1/reemplazos - Creando solicitud de reemplazo");
        ReemplazoDispositivo reemplazo = ReemplazoDispositivoMapper.toEntity(request);
        ReemplazoDispositivo created = reemplazoService.crear(reemplazo);
        ReemplazoDispositivoResponse response = ReemplazoDispositivoMapper.toResponse(created);
        log.info("Solicitud de reemplazo creada exitosamente con ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/ejecutar")
    @Operation(summary = "Ejecutar reemplazo aprobado")
    public ResponseEntity<ReemplazoDispositivoResponse> ejecutarReemplazo(@PathVariable Integer id) {
        log.info("POST /api/v1/reemplazos/{}/ejecutar - Ejecutando reemplazo", id);
        ReemplazoDispositivo executed = reemplazoService.ejecutarReemplazo(id);
        ReemplazoDispositivoResponse response = ReemplazoDispositivoMapper.toResponse(executed);
        log.info("Reemplazo ejecutado exitosamente con ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar solicitud de reemplazo")
    public ResponseEntity<Void> cancelar(
            @PathVariable Integer id,
            @RequestParam String motivo) {
        log.info("DELETE /api/v1/reemplazos/{} - Cancelando reemplazo", id);
        reemplazoService.cancelar(id, motivo);
        log.info("Reemplazo cancelado exitosamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
