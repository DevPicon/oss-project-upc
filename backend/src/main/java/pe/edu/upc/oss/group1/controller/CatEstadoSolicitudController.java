package pe.edu.upc.oss.group1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.catalogo.request.CatEstadoSolicitudRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoSolicitudResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoSolicitud;
import pe.edu.upc.oss.group1.mapper.CatEstadoSolicitudMapper;
import pe.edu.upc.oss.group1.service.CatEstadoSolicitudService;

import java.util.List;

/**
 * Controlador REST para el catálogo de estados de solicitud.
 * Expone endpoints CRUD con validación de DTOs.
 */
@RestController
@RequestMapping("/api/v1/catalogos/estados-solicitud")
@RequiredArgsConstructor
@Slf4j
public class CatEstadoSolicitudController {

    private final CatEstadoSolicitudService catEstadoSolicitudService;

    @GetMapping
    public ResponseEntity<List<CatEstadoSolicitudResponse>> findAll() {
        log.debug("REST request to get all estados de solicitud");
        List<CatEstadoSolicitud> estados = catEstadoSolicitudService.findAll();
        return ResponseEntity.ok(CatEstadoSolicitudMapper.toResponseList(estados));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CatEstadoSolicitudResponse>> findAllActive() {
        log.debug("REST request to get active estados de solicitud");
        List<CatEstadoSolicitud> estados = catEstadoSolicitudService.findAllActive();
        return ResponseEntity.ok(CatEstadoSolicitudMapper.toResponseList(estados));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatEstadoSolicitudResponse> findById(@PathVariable Integer id) {
        log.debug("REST request to get estado de solicitud by ID: {}", id);
        CatEstadoSolicitud estado = catEstadoSolicitudService.findById(id);
        return ResponseEntity.ok(CatEstadoSolicitudMapper.toResponse(estado));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CatEstadoSolicitudResponse> findByCodigo(@PathVariable String codigo) {
        log.debug("REST request to get estado de solicitud by codigo: {}", codigo);
        CatEstadoSolicitud estado = catEstadoSolicitudService.findByCodigo(codigo);
        return ResponseEntity.ok(CatEstadoSolicitudMapper.toResponse(estado));
    }

    @PostMapping
    public ResponseEntity<CatEstadoSolicitudResponse> create(@Valid @RequestBody CatEstadoSolicitudRequest request) {
        log.debug("REST request to create estado de solicitud: {}", request.getCodigo());
        CatEstadoSolicitud estado = CatEstadoSolicitudMapper.toEntity(request);
        CatEstadoSolicitud created = catEstadoSolicitudService.create(estado);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CatEstadoSolicitudMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatEstadoSolicitudResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatEstadoSolicitudRequest request) {
        log.debug("REST request to update estado de solicitud: {}", id);
        CatEstadoSolicitud estado = CatEstadoSolicitudMapper.toEntity(request);
        CatEstadoSolicitud updated = catEstadoSolicitudService.update(id, estado);
        return ResponseEntity.ok(CatEstadoSolicitudMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("REST request to delete estado de solicitud: {}", id);
        catEstadoSolicitudService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CatEstadoSolicitudResponse>> search(@RequestParam String nombre) {
        log.debug("REST request to search estados de solicitud by nombre: {}", nombre);
        List<CatEstadoSolicitud> estados = catEstadoSolicitudService.search(nombre);
        return ResponseEntity.ok(CatEstadoSolicitudMapper.toResponseList(estados));
    }
}
