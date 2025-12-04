package pe.edu.upc.oss.group1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.catalogo.request.CatEstadoReemplazoRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoReemplazoResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoReemplazo;
import pe.edu.upc.oss.group1.mapper.CatEstadoReemplazoMapper;
import pe.edu.upc.oss.group1.service.CatEstadoReemplazoService;

import java.util.List;

/**
 * Controlador REST para el catálogo de estados de reemplazo.
 * Expone endpoints CRUD con validación de DTOs.
 */
@RestController
@RequestMapping("/api/v1/catalogos/estados-reemplazo")
@RequiredArgsConstructor
@Slf4j
public class CatEstadoReemplazoController {

    private final CatEstadoReemplazoService catEstadoReemplazoService;

    @GetMapping
    public ResponseEntity<List<CatEstadoReemplazoResponse>> findAll() {
        log.debug("REST request to get all estados de reemplazo");
        List<CatEstadoReemplazo> estados = catEstadoReemplazoService.findAll();
        return ResponseEntity.ok(CatEstadoReemplazoMapper.toResponseList(estados));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CatEstadoReemplazoResponse>> findAllActive() {
        log.debug("REST request to get active estados de reemplazo");
        List<CatEstadoReemplazo> estados = catEstadoReemplazoService.findAllActive();
        return ResponseEntity.ok(CatEstadoReemplazoMapper.toResponseList(estados));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatEstadoReemplazoResponse> findById(@PathVariable Integer id) {
        log.debug("REST request to get estado de reemplazo by ID: {}", id);
        CatEstadoReemplazo estado = catEstadoReemplazoService.findById(id);
        return ResponseEntity.ok(CatEstadoReemplazoMapper.toResponse(estado));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CatEstadoReemplazoResponse> findByCodigo(@PathVariable String codigo) {
        log.debug("REST request to get estado de reemplazo by codigo: {}", codigo);
        CatEstadoReemplazo estado = catEstadoReemplazoService.findByCodigo(codigo);
        return ResponseEntity.ok(CatEstadoReemplazoMapper.toResponse(estado));
    }

    @PostMapping
    public ResponseEntity<CatEstadoReemplazoResponse> create(@Valid @RequestBody CatEstadoReemplazoRequest request) {
        log.debug("REST request to create estado de reemplazo: {}", request.getCodigo());
        CatEstadoReemplazo estado = CatEstadoReemplazoMapper.toEntity(request);
        CatEstadoReemplazo created = catEstadoReemplazoService.create(estado);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CatEstadoReemplazoMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatEstadoReemplazoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatEstadoReemplazoRequest request) {
        log.debug("REST request to update estado de reemplazo: {}", id);
        CatEstadoReemplazo estado = CatEstadoReemplazoMapper.toEntity(request);
        CatEstadoReemplazo updated = catEstadoReemplazoService.update(id, estado);
        return ResponseEntity.ok(CatEstadoReemplazoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("REST request to delete estado de reemplazo: {}", id);
        catEstadoReemplazoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CatEstadoReemplazoResponse>> search(@RequestParam String nombre) {
        log.debug("REST request to search estados de reemplazo by nombre: {}", nombre);
        List<CatEstadoReemplazo> estados = catEstadoReemplazoService.search(nombre);
        return ResponseEntity.ok(CatEstadoReemplazoMapper.toResponseList(estados));
    }
}
