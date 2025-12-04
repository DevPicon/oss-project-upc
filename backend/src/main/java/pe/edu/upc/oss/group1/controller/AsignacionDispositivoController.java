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
import pe.edu.upc.oss.group1.dto.mapper.AsignacionDispositivoMapper;
import pe.edu.upc.oss.group1.dto.request.AsignacionDispositivoRequest;
import pe.edu.upc.oss.group1.dto.response.AsignacionDispositivoResponse;
import pe.edu.upc.oss.group1.entity.AsignacionDispositivo;
import pe.edu.upc.oss.group1.service.AsignacionDispositivoService;

import java.util.List;

/**
 * Controller REST para gestión de asignaciones de dispositivos a empleados.
 */
@RestController
@RequestMapping("/api/v1/asignaciones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Asignaciones", description = "Gestión de asignaciones de dispositivos a empleados")
public class AsignacionDispositivoController {

    private final AsignacionDispositivoService asignacionService;

    @GetMapping
    @Operation(summary = "Listar todas las asignaciones")
    public ResponseEntity<List<AsignacionDispositivoResponse>> findAll() {
        log.info("GET /api/v1/asignaciones - Obteniendo todas las asignaciones");
        List<AsignacionDispositivo> asignaciones = asignacionService.findAll();
        List<AsignacionDispositivoResponse> response = AsignacionDispositivoMapper.toResponseList(asignaciones);
        log.info("Se encontraron {} asignaciones", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activas")
    @Operation(summary = "Listar asignaciones activas")
    public ResponseEntity<List<AsignacionDispositivoResponse>> findAllActivas() {
        log.info("GET /api/v1/asignaciones/activas - Obteniendo asignaciones activas");
        List<AsignacionDispositivo> asignaciones = asignacionService.findAllActivas();
        List<AsignacionDispositivoResponse> response = AsignacionDispositivoMapper.toResponseList(asignaciones);
        log.info("Se encontraron {} asignaciones activas", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activas/page")
    @Operation(summary = "Listar asignaciones activas con paginación")
    public ResponseEntity<Page<AsignacionDispositivoResponse>> findAllActivasPaginated(Pageable pageable) {
        log.info("GET /api/v1/asignaciones/activas/page - Obteniendo asignaciones activas con paginación");
        Page<AsignacionDispositivo> asignaciones = asignacionService.findAllActivas(pageable);
        Page<AsignacionDispositivoResponse> response = asignaciones.map(AsignacionDispositivoMapper::toResponse);
        log.info("Se encontraron {} asignaciones activas en página {}", response.getContent().size(), pageable.getPageNumber());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener asignación por ID")
    public ResponseEntity<AsignacionDispositivoResponse> findById(@PathVariable Integer id) {
        log.info("GET /api/v1/asignaciones/{} - Obteniendo asignación por ID", id);
        AsignacionDispositivo asignacion = asignacionService.findByIdWithRelations(id);
        AsignacionDispositivoResponse response = AsignacionDispositivoMapper.toResponse(asignacion);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empleado/{empleadoId}")
    @Operation(summary = "Listar asignaciones de un empleado")
    public ResponseEntity<List<AsignacionDispositivoResponse>> findByEmpleado(@PathVariable Integer empleadoId) {
        log.info("GET /api/v1/asignaciones/empleado/{} - Obteniendo asignaciones del empleado", empleadoId);
        List<AsignacionDispositivo> asignaciones = asignacionService.findByEmpleado(empleadoId);
        List<AsignacionDispositivoResponse> response = AsignacionDispositivoMapper.toResponseList(asignaciones);
        log.info("Se encontraron {} asignaciones para el empleado {}", response.size(), empleadoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empleado/{empleadoId}/activas")
    @Operation(summary = "Listar asignaciones activas de un empleado")
    public ResponseEntity<List<AsignacionDispositivoResponse>> findActivasByEmpleado(@PathVariable Integer empleadoId) {
        log.info("GET /api/v1/asignaciones/empleado/{}/activas - Obteniendo asignaciones activas del empleado", empleadoId);
        List<AsignacionDispositivo> asignaciones = asignacionService.findActivasByEmpleado(empleadoId);
        List<AsignacionDispositivoResponse> response = AsignacionDispositivoMapper.toResponseList(asignaciones);
        log.info("Se encontraron {} asignaciones activas para el empleado {}", response.size(), empleadoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dispositivo/{dispositivoId}")
    @Operation(summary = "Listar historial de asignaciones de un dispositivo")
    public ResponseEntity<List<AsignacionDispositivoResponse>> findByDispositivo(@PathVariable Integer dispositivoId) {
        log.info("GET /api/v1/asignaciones/dispositivo/{} - Obteniendo asignaciones del dispositivo", dispositivoId);
        List<AsignacionDispositivo> asignaciones = asignacionService.findByDispositivo(dispositivoId);
        List<AsignacionDispositivoResponse> response = AsignacionDispositivoMapper.toResponseList(asignaciones);
        log.info("Se encontraron {} asignaciones para el dispositivo {}", response.size(), dispositivoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Crear nueva asignación")
    public ResponseEntity<AsignacionDispositivoResponse> create(@Valid @RequestBody AsignacionDispositivoRequest request) {
        log.info("POST /api/v1/asignaciones - Creando asignación de dispositivo {} a empleado {}",
                request.getDispositivoId(), request.getEmpleadoId());
        AsignacionDispositivo asignacion = AsignacionDispositivoMapper.toEntity(request);
        AsignacionDispositivo created = asignacionService.crear(asignacion);
        AsignacionDispositivoResponse response = AsignacionDispositivoMapper.toResponse(created);
        log.info("Asignación creada exitosamente con ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/devolucion")
    @Operation(summary = "Registrar devolución de dispositivo asignado")
    public ResponseEntity<AsignacionDispositivoResponse> registrarDevolucion(
            @PathVariable Integer id,
            @RequestParam(required = false) String observaciones,
            @RequestParam(required = false) Integer usuarioRecibeId) {
        log.info("POST /api/v1/asignaciones/{}/devolucion - Registrando devolución", id);
        AsignacionDispositivo updated = asignacionService.registrarDevolucion(id, observaciones, usuarioRecibeId);
        AsignacionDispositivoResponse response = AsignacionDispositivoMapper.toResponse(updated);
        log.info("Devolución registrada exitosamente para asignación {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar asignación activa")
    public ResponseEntity<Void> cancelar(
            @PathVariable Integer id,
            @RequestParam String motivo) {
        log.info("DELETE /api/v1/asignaciones/{} - Cancelando asignación", id);
        asignacionService.cancelar(id, motivo);
        log.info("Asignación cancelada exitosamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
