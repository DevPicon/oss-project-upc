package pe.edu.upc.oss.group1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.mapper.HistorialDispositivoMapper;
import pe.edu.upc.oss.group1.dto.response.HistorialDispositivoResponse;
import pe.edu.upc.oss.group1.entity.HistorialDispositivo;
import pe.edu.upc.oss.group1.service.HistorialDispositivoService;

import java.util.List;

/**
 * Controller REST para consulta del historial de dispositivos.
 */
@RestController
@RequestMapping("/api/v1/historial-dispositivos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Historial", description = "Auditoría y seguimiento de movimientos de dispositivos")
public class HistorialDispositivoController {

    private final HistorialDispositivoService historialService;

    @GetMapping("/dispositivo/{dispositivoId}")
    @Operation(summary = "Obtener historial completo de un dispositivo")
    public ResponseEntity<List<HistorialDispositivoResponse>> findByDispositivo(@PathVariable Integer dispositivoId) {
        log.info("GET /api/v1/historial-dispositivos/dispositivo/{} - Obteniendo historial completo", dispositivoId);
        List<HistorialDispositivo> historial = historialService.findByDispositivo(dispositivoId);
        List<HistorialDispositivoResponse> response = HistorialDispositivoMapper.toResponseList(historial);
        log.info("Se encontraron {} registros de historial para el dispositivo {}", response.size(), dispositivoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dispositivo/{dispositivoId}/page")
    @Operation(summary = "Obtener historial de un dispositivo con paginación")
    public ResponseEntity<Page<HistorialDispositivoResponse>> findByDispositivoPaginated(
            @PathVariable Integer dispositivoId,
            Pageable pageable) {
        log.info("GET /api/v1/historial-dispositivos/dispositivo/{}/page - Obteniendo historial con paginación", dispositivoId);
        Page<HistorialDispositivo> historial = historialService.findByDispositivo(dispositivoId, pageable);
        Page<HistorialDispositivoResponse> response = historial.map(HistorialDispositivoMapper::toResponse);
        log.info("Se encontraron {} registros de historial en página {} para el dispositivo {}",
                response.getContent().size(), pageable.getPageNumber(), dispositivoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dispositivo/{dispositivoId}/ultimos/{cantidad}")
    @Operation(summary = "Obtener últimos N movimientos de un dispositivo")
    public ResponseEntity<List<HistorialDispositivoResponse>> findUltimosMovimientos(
            @PathVariable Integer dispositivoId,
            @PathVariable int cantidad) {
        log.info("GET /api/v1/historial-dispositivos/dispositivo/{}/ultimos/{} - Obteniendo últimos movimientos",
                dispositivoId, cantidad);
        List<HistorialDispositivo> historial = historialService.findUltimosMovimientos(dispositivoId, cantidad);
        List<HistorialDispositivoResponse> response = HistorialDispositivoMapper.toResponseList(historial);
        log.info("Se obtuvieron {} movimientos recientes para el dispositivo {}", response.size(), dispositivoId);
        return ResponseEntity.ok(response);
    }
}
