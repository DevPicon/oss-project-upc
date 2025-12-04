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
import pe.edu.upc.oss.group1.dto.mapper.SolicitudDevolucionMapper;
import pe.edu.upc.oss.group1.dto.request.SolicitudDevolucionRequest;
import pe.edu.upc.oss.group1.dto.response.SolicitudDevolucionResponse;
import pe.edu.upc.oss.group1.entity.SolicitudDevolucion;
import pe.edu.upc.oss.group1.service.SolicitudDevolucionService;

import java.util.List;

/**
 * Controller REST para gestión de solicitudes de devolución de dispositivos.
 */
@RestController
@RequestMapping("/api/v1/solicitudes-devolucion")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Solicitudes Devolución", description = "Gestión de solicitudes de devolución por cese o cambio de empleado")
public class SolicitudDevolucionController {

    private final SolicitudDevolucionService solicitudService;

    @GetMapping
    @Operation(summary = "Listar todas las solicitudes de devolución")
    public ResponseEntity<List<SolicitudDevolucionResponse>> findAll() {
        log.info("GET /api/v1/solicitudes-devolucion - Obteniendo todas las solicitudes");
        List<SolicitudDevolucion> solicitudes = solicitudService.findAll();
        List<SolicitudDevolucionResponse> response = SolicitudDevolucionMapper.toResponseList(solicitudes);
        log.info("Se encontraron {} solicitudes", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Listar solicitudes pendientes")
    public ResponseEntity<List<SolicitudDevolucionResponse>> findPendientes() {
        log.info("GET /api/v1/solicitudes-devolucion/pendientes - Obteniendo solicitudes pendientes");
        List<SolicitudDevolucion> solicitudes = solicitudService.findPendientes();
        List<SolicitudDevolucionResponse> response = SolicitudDevolucionMapper.toResponseList(solicitudes);
        log.info("Se encontraron {} solicitudes pendientes", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pendientes/page")
    @Operation(summary = "Listar solicitudes pendientes con paginación")
    public ResponseEntity<Page<SolicitudDevolucionResponse>> findPendientesPaginated(Pageable pageable) {
        log.info("GET /api/v1/solicitudes-devolucion/pendientes/page - Obteniendo solicitudes pendientes con paginación");
        Page<SolicitudDevolucion> solicitudes = solicitudService.findPendientes(pageable);
        Page<SolicitudDevolucionResponse> response = solicitudes.map(SolicitudDevolucionMapper::toResponse);
        log.info("Se encontraron {} solicitudes pendientes en página {}", response.getContent().size(), pageable.getPageNumber());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/atrasadas")
    @Operation(summary = "Listar solicitudes atrasadas")
    public ResponseEntity<List<SolicitudDevolucionResponse>> findAtrasadas() {
        log.info("GET /api/v1/solicitudes-devolucion/atrasadas - Obteniendo solicitudes atrasadas");
        List<SolicitudDevolucion> solicitudes = solicitudService.findAtrasadas();
        List<SolicitudDevolucionResponse> response = SolicitudDevolucionMapper.toResponseList(solicitudes);
        log.info("Se encontraron {} solicitudes atrasadas", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener solicitud por ID")
    public ResponseEntity<SolicitudDevolucionResponse> findById(@PathVariable Integer id) {
        log.info("GET /api/v1/solicitudes-devolucion/{} - Obteniendo solicitud por ID", id);
        SolicitudDevolucion solicitud = solicitudService.findByIdWithRelations(id);
        SolicitudDevolucionResponse response = SolicitudDevolucionMapper.toResponse(solicitud);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/detalles")
    @Operation(summary = "Obtener solicitud con detalles de dispositivos")
    public ResponseEntity<SolicitudDevolucionResponse> findByIdWithDetalles(@PathVariable Integer id) {
        log.info("GET /api/v1/solicitudes-devolucion/{}/detalles - Obteniendo solicitud con detalles", id);
        SolicitudDevolucion solicitud = solicitudService.findByIdWithDetalles(id);
        SolicitudDevolucionResponse response = SolicitudDevolucionMapper.toResponse(solicitud);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empleado/{empleadoId}")
    @Operation(summary = "Listar solicitudes de un empleado")
    public ResponseEntity<List<SolicitudDevolucionResponse>> findByEmpleado(@PathVariable Integer empleadoId) {
        log.info("GET /api/v1/solicitudes-devolucion/empleado/{} - Obteniendo solicitudes del empleado", empleadoId);
        List<SolicitudDevolucion> solicitudes = solicitudService.findByEmpleado(empleadoId);
        List<SolicitudDevolucionResponse> response = SolicitudDevolucionMapper.toResponseList(solicitudes);
        log.info("Se encontraron {} solicitudes para el empleado {}", response.size(), empleadoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Crear nueva solicitud de devolución")
    public ResponseEntity<SolicitudDevolucionResponse> create(@Valid @RequestBody SolicitudDevolucionRequest request) {
        log.info("POST /api/v1/solicitudes-devolucion - Creando solicitud para empleado {}", request.getEmpleadoId());
        SolicitudDevolucion solicitud = SolicitudDevolucionMapper.toEntity(request);
        SolicitudDevolucion created = solicitudService.crear(solicitud);
        SolicitudDevolucionResponse response = SolicitudDevolucionMapper.toResponse(created);
        log.info("Solicitud creada exitosamente con ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar solicitud de devolución")
    public ResponseEntity<SolicitudDevolucionResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody SolicitudDevolucionRequest request) {
        log.info("PUT /api/v1/solicitudes-devolucion/{} - Actualizando solicitud", id);
        SolicitudDevolucion solicitud = SolicitudDevolucionMapper.toEntity(request);
        SolicitudDevolucion updated = solicitudService.update(id, solicitud);
        SolicitudDevolucionResponse response = SolicitudDevolucionMapper.toResponse(updated);
        log.info("Solicitud actualizada exitosamente con ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/completar")
    @Operation(summary = "Completar solicitud de devolución")
    public ResponseEntity<SolicitudDevolucionResponse> completar(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer usuarioRecibeId) {
        log.info("POST /api/v1/solicitudes-devolucion/{}/completar - Completando solicitud", id);
        SolicitudDevolucion completed = solicitudService.completar(id, usuarioRecibeId);
        SolicitudDevolucionResponse response = SolicitudDevolucionMapper.toResponse(completed);
        log.info("Solicitud completada exitosamente con ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar solicitud de devolución")
    public ResponseEntity<Void> cancelar(
            @PathVariable Integer id,
            @RequestParam String motivo) {
        log.info("DELETE /api/v1/solicitudes-devolucion/{} - Cancelando solicitud", id);
        solicitudService.cancelar(id, motivo);
        log.info("Solicitud cancelada exitosamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
