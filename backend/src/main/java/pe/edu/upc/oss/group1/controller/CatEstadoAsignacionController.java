package pe.edu.upc.oss.group1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.catalogo.request.CatEstadoAsignacionRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoAsignacionResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoAsignacion;
import pe.edu.upc.oss.group1.mapper.CatEstadoAsignacionMapper;
import pe.edu.upc.oss.group1.service.CatEstadoAsignacionService;

import java.util.List;

/**
 * Controlador REST para el catálogo de estados de asignación.
 * Expone endpoints CRUD con validación de DTOs.
 */
@RestController
@RequestMapping("/api/v1/catalogos/estados-asignacion")
@RequiredArgsConstructor
@Slf4j
public class CatEstadoAsignacionController {

    private final CatEstadoAsignacionService catEstadoAsignacionService;

    @GetMapping
    public ResponseEntity<List<CatEstadoAsignacionResponse>> findAll() {
        log.debug("REST request to get all estados de asignacion");
        List<CatEstadoAsignacion> estados = catEstadoAsignacionService.findAll();
        return ResponseEntity.ok(CatEstadoAsignacionMapper.toResponseList(estados));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CatEstadoAsignacionResponse>> findAllActive() {
        log.debug("REST request to get active estados de asignacion");
        List<CatEstadoAsignacion> estados = catEstadoAsignacionService.findAllActive();
        return ResponseEntity.ok(CatEstadoAsignacionMapper.toResponseList(estados));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatEstadoAsignacionResponse> findById(@PathVariable Integer id) {
        log.debug("REST request to get estado de asignacion by ID: {}", id);
        CatEstadoAsignacion estado = catEstadoAsignacionService.findById(id);
        return ResponseEntity.ok(CatEstadoAsignacionMapper.toResponse(estado));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CatEstadoAsignacionResponse> findByCodigo(@PathVariable String codigo) {
        log.debug("REST request to get estado de asignacion by codigo: {}", codigo);
        CatEstadoAsignacion estado = catEstadoAsignacionService.findByCodigo(codigo);
        return ResponseEntity.ok(CatEstadoAsignacionMapper.toResponse(estado));
    }

    @PostMapping
    public ResponseEntity<CatEstadoAsignacionResponse> create(@Valid @RequestBody CatEstadoAsignacionRequest request) {
        log.debug("REST request to create estado de asignacion: {}", request.getCodigo());
        CatEstadoAsignacion estado = CatEstadoAsignacionMapper.toEntity(request);
        CatEstadoAsignacion created = catEstadoAsignacionService.create(estado);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CatEstadoAsignacionMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatEstadoAsignacionResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatEstadoAsignacionRequest request) {
        log.debug("REST request to update estado de asignacion: {}", id);
        CatEstadoAsignacion estado = CatEstadoAsignacionMapper.toEntity(request);
        CatEstadoAsignacion updated = catEstadoAsignacionService.update(id, estado);
        return ResponseEntity.ok(CatEstadoAsignacionMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("REST request to delete estado de asignacion: {}", id);
        catEstadoAsignacionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CatEstadoAsignacionResponse>> search(@RequestParam String nombre) {
        log.debug("REST request to search estados de asignacion by nombre: {}", nombre);
        List<CatEstadoAsignacion> estados = catEstadoAsignacionService.search(nombre);
        return ResponseEntity.ok(CatEstadoAsignacionMapper.toResponseList(estados));
    }
}
