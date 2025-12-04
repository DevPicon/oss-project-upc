package pe.edu.upc.oss.group1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.mapper.DetalleDevolucionMapper;
import pe.edu.upc.oss.group1.dto.request.DetalleDevolucionRequest;
import pe.edu.upc.oss.group1.dto.response.DetalleDevolucionResponse;
import pe.edu.upc.oss.group1.entity.DetalleDevolucion;
import pe.edu.upc.oss.group1.service.DetalleDevolucionService;

import java.util.List;

/**
 * Controller REST para gestión de detalles de devolución.
 */
@RestController
@RequestMapping("/api/v1/detalles-devolucion")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Detalles Devolución", description = "Gestión de dispositivos específicos en solicitudes de devolución")
public class DetalleDevolucionController {

    private final DetalleDevolucionService detalleService;

    @GetMapping("/solicitud/{solicitudId}")
    @Operation(summary = "Listar detalles de una solicitud")
    public ResponseEntity<List<DetalleDevolucionResponse>> findBySolicitud(@PathVariable Integer solicitudId) {
        log.info("GET /api/v1/detalles-devolucion/solicitud/{} - Obteniendo detalles de la solicitud", solicitudId);
        List<DetalleDevolucion> detalles = detalleService.findBySolicitud(solicitudId);
        List<DetalleDevolucionResponse> response = DetalleDevolucionMapper.toResponseList(detalles);
        log.info("Se encontraron {} detalles para la solicitud {}", response.size(), solicitudId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle por ID")
    public ResponseEntity<DetalleDevolucionResponse> findById(@PathVariable Integer id) {
        log.info("GET /api/v1/detalles-devolucion/{} - Obteniendo detalle por ID", id);
        DetalleDevolucion detalle = detalleService.findByIdWithRelations(id);
        DetalleDevolucionResponse response = DetalleDevolucionMapper.toResponse(detalle);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Agregar dispositivo a solicitud de devolución")
    public ResponseEntity<DetalleDevolucionResponse> agregar(@Valid @RequestBody DetalleDevolucionRequest request) {
        log.info("POST /api/v1/detalles-devolucion - Agregando dispositivo {} a solicitud {}",
                request.getDispositivoId(), request.getSolicitudDevolucionId());
        DetalleDevolucion detalle = DetalleDevolucionMapper.toEntity(request);
        DetalleDevolucion created = detalleService.agregar(detalle);
        DetalleDevolucionResponse response = DetalleDevolucionMapper.toResponse(created);
        log.info("Detalle agregado exitosamente con ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar condición de devolución")
    public ResponseEntity<DetalleDevolucionResponse> updateCondicion(
            @PathVariable Integer id,
            @Valid @RequestBody DetalleDevolucionRequest request) {
        log.info("PUT /api/v1/detalles-devolucion/{} - Actualizando condición", id);
        DetalleDevolucion detalle = DetalleDevolucionMapper.toEntity(request);
        DetalleDevolucion updated = detalleService.updateCondicion(id, detalle);
        DetalleDevolucionResponse response = DetalleDevolucionMapper.toResponse(updated);
        log.info("Condición actualizada exitosamente para detalle {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar detalle de devolución")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("DELETE /api/v1/detalles-devolucion/{} - Eliminando detalle", id);
        detalleService.delete(id);
        log.info("Detalle eliminado exitosamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
