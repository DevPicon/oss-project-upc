package pe.edu.upc.oss.group1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.catalogo.request.CatTipoMovimientoRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatTipoMovimientoResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatTipoMovimiento;
import pe.edu.upc.oss.group1.mapper.CatTipoMovimientoMapper;
import pe.edu.upc.oss.group1.service.CatTipoMovimientoService;

import java.util.List;

/**
 * Controlador REST para el catálogo de tipos de movimiento.
 * Expone endpoints CRUD con validación de DTOs.
 */
@RestController
@RequestMapping("/api/v1/catalogos/tipos-movimiento")
@RequiredArgsConstructor
@Slf4j
public class CatTipoMovimientoController {

    private final CatTipoMovimientoService catTipoMovimientoService;

    @GetMapping
    public ResponseEntity<List<CatTipoMovimientoResponse>> findAll() {
        log.debug("REST request to get all tipos de movimiento");
        List<CatTipoMovimiento> tipos = catTipoMovimientoService.findAll();
        return ResponseEntity.ok(CatTipoMovimientoMapper.toResponseList(tipos));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CatTipoMovimientoResponse>> findAllActive() {
        log.debug("REST request to get active tipos de movimiento");
        List<CatTipoMovimiento> tipos = catTipoMovimientoService.findAllActive();
        return ResponseEntity.ok(CatTipoMovimientoMapper.toResponseList(tipos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatTipoMovimientoResponse> findById(@PathVariable Integer id) {
        log.debug("REST request to get tipo de movimiento by ID: {}", id);
        CatTipoMovimiento tipo = catTipoMovimientoService.findById(id);
        return ResponseEntity.ok(CatTipoMovimientoMapper.toResponse(tipo));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CatTipoMovimientoResponse> findByCodigo(@PathVariable String codigo) {
        log.debug("REST request to get tipo de movimiento by codigo: {}", codigo);
        CatTipoMovimiento tipo = catTipoMovimientoService.findByCodigo(codigo);
        return ResponseEntity.ok(CatTipoMovimientoMapper.toResponse(tipo));
    }

    @PostMapping
    public ResponseEntity<CatTipoMovimientoResponse> create(@Valid @RequestBody CatTipoMovimientoRequest request) {
        log.debug("REST request to create tipo de movimiento: {}", request.getCodigo());
        CatTipoMovimiento tipo = CatTipoMovimientoMapper.toEntity(request);
        CatTipoMovimiento created = catTipoMovimientoService.create(tipo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CatTipoMovimientoMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatTipoMovimientoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatTipoMovimientoRequest request) {
        log.debug("REST request to update tipo de movimiento: {}", id);
        CatTipoMovimiento tipo = CatTipoMovimientoMapper.toEntity(request);
        CatTipoMovimiento updated = catTipoMovimientoService.update(id, tipo);
        return ResponseEntity.ok(CatTipoMovimientoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("REST request to delete tipo de movimiento: {}", id);
        catTipoMovimientoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CatTipoMovimientoResponse>> search(@RequestParam String nombre) {
        log.debug("REST request to search tipos de movimiento by nombre: {}", nombre);
        List<CatTipoMovimiento> tipos = catTipoMovimientoService.search(nombre);
        return ResponseEntity.ok(CatTipoMovimientoMapper.toResponseList(tipos));
    }
}
